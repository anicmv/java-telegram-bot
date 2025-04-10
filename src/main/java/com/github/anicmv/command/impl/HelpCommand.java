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
 * @description /help
 */
@Component
public class HelpCommand implements BotCommand {

    @Resource
    private BotConfig config;

    @Override
    public boolean supports(String commandText) {
        return BotUtil.isThisCommand(BotConstant.HELP, commandText.trim(), config);
    }

    @Override
    public SendMessage execute(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String response = """
                Available commands:
                    /help   - 帮助
                    /ping   - 存活测试
                    /fd     - 发癫
                    /diss   - diss
                    /biss   - biss
                    /kfc    - kfc文案
                    /my     - 摸鱼日历
                """;

        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .text(response)
                .build();
    }
}