package by.faeton.helper.controller.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartHandler implements Handler {



    @Override
    public boolean isAppropriateTypeMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().contains(TelegramCommand.START);
        }
        return false;
    }

    @Override
    public List<BotApiMethod> execute(Update update) {
        List<BotApiMethod> sendMessages = new ArrayList<>();
        Long chatId = getChatId(update);

        sendMessages.add(SendMessage.builder()
            .chatId(chatId)
            .text(TelegramDefaultMessages.GREETING)
            .build());
        return sendMessages;
    }

}