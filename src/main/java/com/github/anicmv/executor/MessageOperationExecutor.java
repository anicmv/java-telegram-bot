package com.github.anicmv.executor;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author anicmv
 * @date 2025/3/30 17:38
 * @description 消息额外操作
 */
@Log4j2
@Component
public class MessageOperationExecutor {

    @Resource
    private TelegramClient telegramClient;

    public void execute(PartialBotApiMethod<?> method) throws TelegramApiException {
        if (method instanceof BotApiMethod<?>) {
            telegramClient.execute((BotApiMethod<?>) method);
        } else if (method instanceof EditMessageMedia) {
            telegramClient.execute((EditMessageMedia) method);
        }
    }
}
