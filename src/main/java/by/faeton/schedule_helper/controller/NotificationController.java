package by.faeton.helper.controller;

import by.faeton.helper.controller.handlers.TelegramDefaultMessages;
import by.faeton.helper.exception.ResourceNotFoundException;
import by.faeton.helper.model.Task;
import by.faeton.helper.services.ScheduleService;
import by.faeton.helper.services.SheetListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationController {


    private final ScheduleService scheduleService;
    private final MessageSender messageSender;
    private final List<String> subscribersId;
    private final SheetListener sheetListener;

    private final List<Task> arrived;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void perOneHour() {
        Task nextTask = scheduleService.getNextTask();

        long minutes = Duration.between(LocalDateTime.now(), nextTask.getDate()).toMinutes();

        boolean contains = arrived.contains(nextTask);
        if (minutes < 60 && !contains) {
            arrived.add(nextTask);
            subscribersId.forEach(sub -> messageSender.sendUserMessage(SendMessage.builder()
                .chatId(sub)
                .text(TelegramDefaultMessages.HOUR_NOTICE)
                .build()));
        }
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void perTenMinutes() {
        Task nextTask = scheduleService.getNextTask();

        long minutes = Duration.between(LocalDateTime.now(), nextTask.getDate()).toMinutes();

        boolean contains = arrived.contains(nextTask);
        if (minutes < 10 && !contains) {
            arrived.add(nextTask);
            subscribersId.forEach(sub -> messageSender.sendUserMessage(SendMessage.builder()
                .chatId(sub)
                .text(TelegramDefaultMessages.HOUR_NOTICE)
                .build()));
        }
    }

    @PostConstruct
    private void init() {
        List<List<String>> arrayLists = sheetListener.getSheetList("subs", "A1:A10")
            .orElseThrow(() -> new ResourceNotFoundException("Subscribers not found"));
        subscribersId.addAll(arrayLists.stream()
            .flatMap(Collection::stream)
            .toList());
    }
}
