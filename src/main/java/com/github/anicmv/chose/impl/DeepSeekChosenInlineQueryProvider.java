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


    @Override
    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
        // 内联菜单的id
        return BotConstant.N_3.equals(chosenInlineQuery.getResultId());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();
        String query = chosenInlineQuery.getQuery();
        List<String> parts = StrUtil.split(query, ' ', 2);
        String prompt = parts.getLast();

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                // 根据渠道模型名称动态修改这个参数
                .model(deepSeekProperties.getModel())
                .addUserMessage(prompt).build();

        ChatCompletionResponse response = deepSeekClient.chatCompletion(request).execute();

        String content = response.choices().getFirst().message().content();
        return getOptionalEditMessageText(chosenInlineQuery, content);

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
