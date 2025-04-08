package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 哔哩哔哩每日放送
 */
@Slf4j
@Component
public class BiliTimelineResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_4;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        String imageUrl = "https://jpg.moe/i/hr58gxep.jpeg";

        InlineKeyboardButton gmButton = InlineKeyboardButton.builder()
                .text("国漫")
                .callbackData(BotConstant.CALLBACK_BILI_GM)
                .build();

        InlineKeyboardButton rmButton = InlineKeyboardButton.builder()
                .text("日漫")
                .callbackData(BotConstant.CALLBACK_BILI_RM)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(gmButton);
        row.add(rmButton);
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText("哔哩哔哩每日放送")
                .build();

        return InlineQueryResultArticle.builder()
                .id(getSortId())
                .thumbnailUrl(imageUrl)
                .inputMessageContent(content)
                .title("哔哩哔哩每日放送")
                .replyMarkup(markup)
                .build();
    }

}
