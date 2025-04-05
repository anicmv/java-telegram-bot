package com.github.anicmv.Inline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.entity.Biss;
import com.github.anicmv.mapper.BissMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 2.biss
 */
@Component
public class BissInlineQueryResultProvider implements InlineQueryResultProvider {

    @Resource
    private BissMapper bissMapper;

    @Override
    public String getSortId() {
        return BotConstant.N_4;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {

        Biss biss = bissMapper.selectOne(
                new LambdaQueryWrapper<Biss>().last("ORDER BY RAND() LIMIT 1")
        );

        String text = (biss != null && biss.getContent() != null)
                ? biss.getContent()
                : "没有找到吐槽内容哦！";

        String imageUrl = "https://jpg.moe/i/e0h3qgb4.jpeg";
        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText(text)
                .build();

        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("Biss")
                .inputMessageContent(content)
                .build();

    }
}
