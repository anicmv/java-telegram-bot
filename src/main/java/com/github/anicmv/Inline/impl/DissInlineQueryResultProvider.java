package com.github.anicmv.Inline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.entity.Diss;
import com.github.anicmv.mapper.DissMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 2.文章示例
 */
@Component
public class DissInlineQueryResultProvider implements InlineQueryResultProvider {

    @Resource
    private DissMapper dissMapper;

    @Override
    public String getSortId() {
        return BotConstant.N_2;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {

        Diss diss = dissMapper.selectOne(
                new LambdaQueryWrapper<Diss>().last("ORDER BY RAND() LIMIT 1")
        );

        String text = (diss != null && diss.getContent() != null)
                ? diss.getContent()
                : "没有找到吐槽内容哦！";

        String imageUrl = "https://jpg.moe/i/cjyg088j.webp";
        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText(text)
                .build();

        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("Diss")
                .inputMessageContent(content)
                .build();

    }
}
