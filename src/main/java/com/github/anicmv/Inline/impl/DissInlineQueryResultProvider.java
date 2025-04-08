package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.mapper.DissMapper;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 2.diss
 */
@Component
public class DissInlineQueryResultProvider implements InlineQueryResultProvider {

    @Resource
    private DissMapper dissMapper;

    @Override
    public String getSortId() {
        return BotConstant.N_5;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        String imageUrl = "https://jpg.moe/i/pyzx542e.webp";
        return BotUtil.buildInlineQueryResult(update, config, dissMapper, null, getSortId(), "Diss", imageUrl);
    }
}
