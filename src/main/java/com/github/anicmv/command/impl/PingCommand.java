package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.executor.extend.DeleteMessageExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 16:26
 * @description /ping
 */
@Slf4j
@Component
public class PingCommand implements BotCommand {

    @Resource
    private DeleteMessageExecutor deleteMessageExecutor;

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.PING);
    }

    @Override
    public SendMessage execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer userMessageId = update.getMessage().getMessageId();

        // 调用消息操作执行器删除该消息
        deleteMessageExecutor.execute(chatId, userMessageId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("🏓")
                .build();
    }
}
