package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
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
public class KfcInlineQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_3;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {

        String imageUrl = "https://jpg.moe/i/vfgh1bfq.png";
        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText(BotUtil.kfc())
                .build();

        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("kfc文案")
                .inputMessageContent(content)
                .build();

    }
}
