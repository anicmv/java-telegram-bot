package com.github.anicmv.bot;

import com.github.anicmv.config.BotConfig;
import com.github.anicmv.dispatcher.UpdateDispatcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description bot config
 */
@Log4j2
@Configuration
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UpdateDispatcher updateDispatcher;
    private final BotConfig botConfig;

    public TelegramBot(TelegramClient telegramClient, UpdateDispatcher updateDispatcher, BotConfig botConfig) {
        this.telegramClient = telegramClient;
        this.updateDispatcher = updateDispatcher;
        this.botConfig = botConfig;
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        try {
            PartialBotApiMethod<?> method = updateDispatcher.dispatch(update, telegramClient, botConfig).orElse(null);
            switch (method) {
                case BotApiMethod<?> botApiMethod -> telegramClient.execute(botApiMethod);
                case EditMessageMedia editMessageMedia -> telegramClient.execute(editMessageMedia);
                case SendPhoto sendPhoto -> telegramClient.execute(sendPhoto);
                case SendDocument sendDocument -> telegramClient.execute(sendDocument);
                case null, default -> {
                }
            }

        } catch (TelegramApiException e) {
            log.error("Error executing command: {}", e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
