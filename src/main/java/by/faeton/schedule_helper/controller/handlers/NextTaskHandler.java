package by.faeton.schedule_helper.controller.handlers;

import by.faeton.schedule_helper.controller.TelegramCommand;
import by.faeton.schedule_helper.controller.TelegramDefaultMessages;
import by.faeton.schedule_helper.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NextTaskHandler implements Handler {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", new Locale("ru"));


    private final ScheduleService scheduleService;

    @Override
    public boolean isAppropriateTypeMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().contains(TelegramCommand.NEXT);
        }
        return false;
    }

    @Override
    public List<BotApiMethod> execute(Update update) {
        List<BotApiMethod> sendMessages = new ArrayList<>();
        Long chatId = getChatId(update);

        String message = Optional.of(scheduleService.getNextTask())
            .map(task -> "%s %s %s".formatted(DATE_TIME_FORMATTER.format(task.getDate()), task.getName(), task.getTeacher()))
            .orElse(TelegramDefaultMessages.NEXT_TASK_NOT_FOUND);

        sendMessages.add(SendMessage.builder()
            .chatId(chatId)
            .text(message)
            .build());
        return sendMessages;
    }


}
