package by.faeton.schedule_helper.controller.handlers;

import by.faeton.schedule_helper.controller.TelegramCommand;
import by.faeton.schedule_helper.model.Task;
import by.faeton.schedule_helper.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleHandler implements Handler {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", new Locale("ru"));

    private final ScheduleService scheduleService;

    @Override
    public boolean isAppropriateTypeMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().contains(TelegramCommand.SCHEDULE);
        }
        return false;
    }

    @Override
    public List<BotApiMethod> execute(Update update) {
        List<Task> tasks = scheduleService.getTasks();
        List<BotApiMethod> sendMessages = new ArrayList<>();
        Long chatId = getChatId(update);

        String message = tasks.stream()
            .map(task -> "%s %s %s".formatted(DATE_TIME_FORMATTER.format(task.getDate()), task.getName(), task.getTeacher()))
            .collect(Collectors.joining("\n"));

        sendMessages.add(SendMessage.builder()
            .chatId(chatId)
            .text(message)
            .build());
        return sendMessages;
    }


}
