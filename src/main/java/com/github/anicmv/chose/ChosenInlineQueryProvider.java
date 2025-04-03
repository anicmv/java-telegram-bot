package com.github.anicmv.chose;

import com.github.anicmv.config.BotConfig;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 按钮选择
 */
public interface ChosenInlineQueryProvider {
    /**
     * 判断当前 Provider 是否支持处理该 ChosenInlineQuery
     */
    boolean supports(ChosenInlineQuery chosenInlineQuery);

    /**
     * 根据 ChosenInlineQuery 创建需要执行的 PartialBotApiMethod 结果
     */
    Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException;
}
