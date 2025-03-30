//package com.github.anicmv.Inline.impl;
//
//import com.github.anicmv.Inline.InlineQueryResultProvider;
//import com.github.anicmv.contant.BotConstant;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
//
///**
// * @author anicmv
// * @date 2025/3/30 00:48
// * @description 2.文章示例
// */
//@Component
//public class ArticleInlineQueryResultProvider implements InlineQueryResultProvider {
//
//    @Override
//    public String getSortId() {
//        return BotConstant.N_5;
//    }
//
//    @Override
//    public InlineQueryResult createResult(InlineQuery inlineQuery) {
//        InputTextMessageContent content = InputTextMessageContent.builder()
//                .messageText("这是一个文章内容示例")
//                .build();
//
//        return InlineQueryResultArticle.builder()
//                .id(getSortId())
//                .title("📖 示例文章")
//                .inputMessageContent(content)
//                .build();
//    }
//}
