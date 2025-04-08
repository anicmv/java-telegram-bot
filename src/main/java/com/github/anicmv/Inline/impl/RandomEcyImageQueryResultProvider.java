package com.github.anicmv.Inline.impl;//package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 1.随机图片
 */
@Component
class RandomEcyImageQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_1;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        InlineQuery inlineQuery = update.getInlineQuery();
        String imageUrl = "https://jpg.moe/i/rp8dpcn2.jpeg";

        String fullName = inlineQuery.getFrom().getFirstName() +
                (inlineQuery.getFrom().getLastName() == null ? "" : inlineQuery.getFrom().getLastName());

        // 同时附加内联键盘按钮实现交互
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("\uD83D\uDE0D" + fullName + "\uD83D\uDE0D")
                .callbackData(BotConstant.CALLBACK_RANDOM_ECY)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button);
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();


        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("随机二次元")
                .replyMarkup(markup)
                .build();
    }


}
