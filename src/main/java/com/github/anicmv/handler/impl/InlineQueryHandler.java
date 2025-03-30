package com.github.anicmv.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.github.anicmv.Inline.InlineQueryResultProvider;
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
    public Optional<PartialBotApiMethod<?>> handle(Update update) {
        InlineQuery inlineQuery = update.getInlineQuery();
        String query = inlineQuery.getQuery();

        List<InlineQueryResult> results = resultProviders.stream()
                .filter(provider -> {
                    // 如果查询不为空，则只保留 sortId 等于 BotConstant.N_1 的 provider，否则全部保留
                    return StrUtil.isEmpty(query) || BotConstant.N_1.equals(provider.getSortId());
                })
                .sorted(Comparator.comparing(InlineQueryResultProvider::getSortId))
                .map(provider -> provider.createResult(inlineQuery))
                .toList();

        AnswerInlineQuery answer = AnswerInlineQuery.builder()
                .inlineQueryId(inlineQuery.getId())
                .results(results)
                .cacheTime(1)
                .build();

        return Optional.of(answer);
    }
}