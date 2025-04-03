package com.github.anicmv.handler.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 文本信息处理器
 */
@Component
public class TextMessageHandler implements UpdateHandler {

    private final List<BotCommand> botCommands;

    @Autowired
    public TextMessageHandler(List<BotCommand> botCommands) {
        this.botCommands = botCommands;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) {
        String text = update.getMessage().getText().trim();

        Optional<BotCommand> commandOpt = botCommands.stream()
                .filter(cmd -> cmd.supports(text))
                .findFirst();

        return commandOpt.map(botCommand -> botCommand.execute(update));
    }
}