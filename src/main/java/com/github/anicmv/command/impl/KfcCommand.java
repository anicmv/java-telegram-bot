package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /kfc
 */
@Component
public class KfcCommand implements BotCommand {

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.KFC);
    }

    @Override
    public SendMessage execute(Update update) {
        long chatId = update.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .text(BotUtil.kfc())
                .build();
    }
}