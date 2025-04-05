package com.github.anicmv.handler.impl;

import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.handler.UpdateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 回调查询处理器
 */
@Log4j2
@Component
public class CallbackQueryHandler implements UpdateHandler {

    // 使用 ConcurrentHashMap 存储每个用户的锁 (key: Telegram user id) (不起作用
    private static final ConcurrentMap<Long, ReentrantLock> USER_LOCKS = new ConcurrentHashMap<>();

    private final List<CallbackQueryProvider> providers;

    @Autowired
    public CallbackQueryHandler(List<CallbackQueryProvider> providers) {
        this.providers = providers;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        long userId = callbackQuery.getFrom().getId();

        // 为当前用户获取一个锁对象
        ReentrantLock lock = USER_LOCKS.computeIfAbsent(userId, id -> new ReentrantLock());

        lock.lock();
        try {
            for (CallbackQueryProvider provider : providers) {
                if (provider.supports(callbackQuery)) {
                    Optional<PartialBotApiMethod<?>> result = provider.handle(update, client, config);
                    if (result.isPresent() && result.get() instanceof EditMessageMedia) {
                        return result;
                    }
                }
            }
            return Optional.empty();
        } finally {
            // 处理完成后释放锁
            lock.unlock();
            USER_LOCKS.compute(userId, (key, currentLock) -> {
                if (currentLock != null && !currentLock.isLocked() && !currentLock.hasQueuedThreads()) {
                    return null;
                }
                return currentLock;
            });
            USER_LOCKS.remove(userId, lock);
        }
    }

}