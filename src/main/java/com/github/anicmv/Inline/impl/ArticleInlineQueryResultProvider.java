package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 7.文章示例
 */
@Component
public class ArticleInlineQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_99;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {
        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText("这里什么都没有...")
                .build();

        return InlineQueryResultArticle.builder()
                .id(getSortId())
                .title("\uD83C\uDF5A 如何食用")
                .inputMessageContent(content)
                .build();
    }
}
