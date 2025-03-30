package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /help
 */
@Component
public class HelpCommand implements BotCommand {

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.HELP);
    }

    @Override
    public SendMessage execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String response = """
                Available commands:
                  /help    - Show available commands
                  /hello   - Greets you
                  /ping    - 存活测试
                  /diss    - 骂人的
                  /biss    - 骂人的的
                  /fadian  - 发癫
                  /kfc     - kfc文案
                """;
        return SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
    }
}