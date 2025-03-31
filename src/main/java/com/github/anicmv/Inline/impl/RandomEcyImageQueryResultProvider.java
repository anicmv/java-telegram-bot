package com.github.anicmv.Inline.impl;//package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

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
    public InlineQueryResult createResult(InlineQuery inlineQuery) {
        String imageUrl = "https://jpg.moe/i/4rtnoeo1.jpeg";
        // 同时附加内联键盘按钮实现交互
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("随机ecy")
                .callbackData(BotConstant.CALLBACK_RANDOM_ECY)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button);
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        // 构造一个可点击的用户名字符串，使用 Markdown 格式。通过tg://user?id=用户ID链接到用户详情页
        String fullName = inlineQuery.getFrom().getFirstName() +
                (inlineQuery.getFrom().getLastName() == null ? "" : inlineQuery.getFrom().getLastName());
        String clickableUsername = "[" + fullName + "](tg://user?id=" + inlineQuery.getFrom().getId() + ")";

        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("随机ecy")
                .caption("亲爱的" + clickableUsername + "\n涩图\uD83D\uDC47点击即送!")
                .parseMode("markdown")
                .replyMarkup(markup)
                .build();
    }


}
