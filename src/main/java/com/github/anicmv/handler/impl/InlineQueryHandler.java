package com.github.anicmv.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.handler.UpdateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 内联查询处理器
 */
@Log4j2
@Component
public class InlineQueryHandler implements UpdateHandler {

    private final List<InlineQueryResultProvider> resultProviders;

    @Autowired
    public InlineQueryHandler(List<InlineQueryResultProvider> resultProviders) {
        this.resultProviders = resultProviders;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasInlineQuery();
    }


    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        InlineQuery inlineQuery = update.getInlineQuery();
        String query = inlineQuery.getQuery();
        Long id = inlineQuery.getFrom().getId();
        List<String> whitelist = Arrays.stream(config.getWhitelist().split(",")).toList();

        List<InlineQueryResult> results = resultProviders.stream()
                .filter(provider -> queryFilter(provider, query))
                .filter(provider -> whitelistFilter(provider, id, whitelist))
                .sorted(Comparator.comparing(InlineQueryResultProvider::getSortId))
                .map(provider -> provider.createResult(update, client, config))
                .toList();

        AnswerInlineQuery answer = AnswerInlineQuery.builder()
                .inlineQueryId(inlineQuery.getId())
                .results(results)
                .cacheTime(1)
                .build();

        return Optional.of(answer);
    }

    /**
     * 根据查询内容筛选 provider
     */
    private boolean queryFilter(InlineQueryResultProvider provider, String query) {
        return StrUtil.isEmpty(query)
                || (query.startsWith("fd") && BotConstant.N_7.equals(provider.getSortId()))
                || (query.startsWith("ds") && BotConstant.N_3.equals(provider.getSortId()));
    }

    /**
     * 对 provider.getSortId() 为 "5" 或 "6" 的结果，判断用户 id 是否在 whitelist 内
     */
    private boolean whitelistFilter(InlineQueryResultProvider provider, Long id, List<String> whitelist) {
        if ("5".equals(provider.getSortId()) || "6".equals(provider.getSortId())) {
            return whitelist.contains(String.valueOf(id));
        }
        return true;
    }
}