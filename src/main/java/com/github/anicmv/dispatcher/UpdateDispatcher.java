package com.github.anicmv.dispatcher;

import com.github.anicmv.config.BotConfig;
import com.github.anicmv.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 命令调度器
 */
@Component
public class UpdateDispatcher {

    private final List<UpdateHandler> updateHandlers;

    @Autowired
    public UpdateDispatcher(List<UpdateHandler> updateHandlers) {
        this.updateHandlers = updateHandlers;
    }

    /**
     * 根据 update 调用匹配的 Handler 处理消息，返回一个 Optional 包装的 PartialBotApiMethod 对象
     */
    public Optional<PartialBotApiMethod<?>> dispatch(Update update, TelegramClient client, BotConfig botConfig) throws TelegramApiException {
        for (UpdateHandler handler : updateHandlers) {
            if (handler.supports(update)) {
                return handler.handle(update, client, botConfig);
            }
        }
        return Optional.empty();
    }
}