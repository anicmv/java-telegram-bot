package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.mapper.BissMapper;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.generics.TelegramClient;

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
        return BotConstant.N_6;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        String imageUrl = "https://jpg.moe/i/e0h3qgb4.jpeg";
        return BotUtil.buildInlineQueryResult(update, config, null, bissMapper, getSortId(), "Biss", imageUrl);
    }
}
