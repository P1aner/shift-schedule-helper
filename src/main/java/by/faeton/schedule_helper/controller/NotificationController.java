package by.faeton.schedule_helper.controller;

import by.faeton.schedule_helper.config.BotConfig;
import by.faeton.schedule_helper.exception.ResourceNotFoundException;
import by.faeton.schedule_helper.model.Task;
import by.faeton.schedule_helper.services.ScheduleService;
import by.faeton.schedule_helper.services.SheetListener;
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
    private final BotConfig botConfig;

    private final List<Task> arrivedPerHour;
    private final List<Task> arrivedPerTenMinutes;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void perOneHour() {
        arrive(arrivedPerHour, 60, TelegramDefaultMessages.HOUR_NOTICE);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void perTenMinutes() {
        arrive(arrivedPerTenMinutes, 10, TelegramDefaultMessages.TEN_MINUTES_NOTICE);
    }

    private void arrive(List<Task> arrivedPerX, int x, String message) {
        Task nextTask = scheduleService.getNextTask();
        long minutes = Duration.between(LocalDateTime.now(), nextTask.getDate()).toMinutes();

        boolean contains = arrivedPerX.contains(nextTask);
        if (minutes < x && !contains) {
            arrivedPerX.add(nextTask);
            subscribersId.forEach(sub -> messageSender.sendUserMessage(SendMessage.builder()
                .chatId(sub)
                .text(message)
                .build()));
        }
    }

    @PostConstruct
    private void init() {
        List<List<String>> arrayLists = sheetListener.getSheetList(botConfig.sheetName(), botConfig.range())
            .orElseThrow(() -> new ResourceNotFoundException("Subscribers not found"));
        subscribersId.addAll(arrayLists.stream()
            .flatMap(Collection::stream)
            .toList());
    }
}
