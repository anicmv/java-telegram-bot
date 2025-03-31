package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;

import java.util.Map;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 8.摸鱼日历
 */
@Log4j2
@Component
public class MoyuInlineQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_8;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {

        String imageUrl = getImageUrl();
        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("摸鱼日历")
                .build();
    }


    private String getImageUrl() {
        String moYuApi = "https://api.vvhan.com/api/moyu";
        String redirectUrl = HttpUtil.redirectUrl(moYuApi, Map.of());
        if (redirectUrl == null) {
            log.error("Error occurred during redirect URL");
        }
        return redirectUrl;
    }
}
