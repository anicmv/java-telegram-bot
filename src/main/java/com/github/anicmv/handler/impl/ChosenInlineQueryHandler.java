package com.github.anicmv.handler.impl;

import com.github.anicmv.chose.ChosenInlineQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.handler.UpdateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 回调查询处理器
 */
@Log4j2
@Component
public class ChosenInlineQueryHandler implements UpdateHandler {

    private final List<ChosenInlineQueryProvider> providers;

    @Autowired
    public ChosenInlineQueryHandler(List<ChosenInlineQueryProvider> providers) {
        this.providers = providers;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasChosenInlineQuery();
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();
        for (ChosenInlineQueryProvider provider : providers) {
            if (provider.supports(chosenInlineQuery)) {
                Optional<PartialBotApiMethod<?>> result = provider.handle(update, client, config);
                if (result.isPresent() && result.get() instanceof EditMessageMedia) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }
}

