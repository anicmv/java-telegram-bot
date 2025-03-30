package com.github.anicmv.callback;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 回调查询提供者
 */
public interface CallbackQueryProvider {
    /**
     * 判断当前 Provider 是否支持处理该 CallbackQuery
     */
    boolean supports(CallbackQuery callbackQuery);

    /**
     * 处理 CallbackQuery，并返回需要执行的 PartialBotApiMethod 结果
     */
    Optional<PartialBotApiMethod<?>> handle(Update update);
}