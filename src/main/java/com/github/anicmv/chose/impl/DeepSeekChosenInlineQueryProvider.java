package com.github.anicmv.chose.impl;

import cn.hutool.core.util.StrUtil;
import com.github.anicmv.chose.ChosenInlineQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import io.github.pigmesh.ai.deepseek.config.DeepSeekProperties;
import io.github.pigmesh.ai.deepseek.core.DeepSeekClient;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionRequest;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionResponse;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description deepseek
 */
@Log4j2
@Component
public class DeepSeekChosenInlineQueryProvider implements ChosenInlineQueryProvider {

    @Resource
    private DeepSeekProperties deepSeekProperties;

    @Resource
    private DeepSeekClient deepSeekClient;

    // 线程池字段，用于异步执行 deepSeek 请求
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
        // 内联菜单的id
        return BotConstant.N_3.equals(chosenInlineQuery.getResultId());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();

        // 获取用户输入
        String query = chosenInlineQuery.getQuery();
        List<String> parts = StrUtil.split(query, ' ', 2);
        String prompt = parts.getLast();

        // 异步调用 deepSeek 接口
        executorService.submit(() -> {
            try {
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                        // 根据渠道模型名称动态修改这个参数
                        .model(deepSeekProperties.getModel())
                        .addUserMessage(prompt)
                        .build();

                ChatCompletionResponse response = deepSeekClient.chatCompletion(request).execute();
                String content = response.choices().getFirst().message().content();

                // 构造更新后的消息
                Optional<PartialBotApiMethod<?>> updatedResponse = getOptionalEditMessageText(chosenInlineQuery, content);
                // 通过 Telegram 客户端异步调用更新消息接口
                updatedResponse.ifPresent(method -> {
                    try {
                        client.execute((EditMessageText) method);
                    } catch (TelegramApiException e) {
                        log.error("Failed to update message", e);
                    }
                });
            } catch (Exception e) {
                log.error("DeepSeek asynchronous processing failed", e);
            }
        });

        // 返回初始响应，保证 Telegram 内联查询不超时
        return Optional.empty();
    }

    public static Optional<PartialBotApiMethod<?>> getOptionalEditMessageText(ChosenInlineQuery chosenInlineQuery, String content) {
        String inlineMessageId = chosenInlineQuery.getInlineMessageId();
        EditMessageText editMessageText = EditMessageText.builder()
                .inlineMessageId(inlineMessageId)
                .text(BotUtil.removeMarkdownV2(content))
                .build();
        return Optional.of(editMessageText);
    }
}


