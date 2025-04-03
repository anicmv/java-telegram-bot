package com.github.anicmv.handler;

import com.github.anicmv.config.BotConfig;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 信息接口
 */
public interface UpdateHandler {
    /**
     * 判断此 Handler 是否支持处理该 Update 对象
     */
    boolean supports(Update update);

    /**
     * 处理 Update，返回一个 Optional 包装的 BotApiMethod 对象
     * 如 SendMessage、AnswerInlineQuery 等回复
     */
    Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig botConfig) throws TelegramApiException;
}