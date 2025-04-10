package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /kfc
 */
@Component
public class KfcCommand implements BotCommand {

    @Resource
    private BotConfig config;

    @Override
    public boolean supports(String commandText) {
        return BotUtil.isThisCommand(BotConstant.KFC, commandText.trim(), config);
    }

    @Override
    public SendMessage execute(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .text(BotUtil.kfc())
                .build();
    }
}