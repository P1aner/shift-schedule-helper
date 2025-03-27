package by.faeton.schedule_helper.controller;

import by.faeton.schedule_helper.config.BotConfig;
import by.faeton.schedule_helper.controller.handlers.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageBroker implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final List<Handler> handlers;

    @Override
    public void consume(Update update) {
        List<BotApiMethod> collect = handlers.stream()
            .filter(handler -> handler.isAppropriateTypeMessage(update))
            .flatMap(handler -> handler.execute(update).stream())
            .toList();

        if (collect.size() != 1) {
            log.warn("Messages is not one. Count: {}", collect.size());
        } else {
            collect
                .forEach(messageSender::sendUserMessage);
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.botToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
