package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /hello
 */
@Component
public class HelloCommand implements BotCommand {

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.HELLO);
    }

    @Override
    public SendMessage execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String response = "Hello! How can I help you today?";
        return SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
    }
}