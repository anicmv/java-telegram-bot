package com.github.anicmv.Inline.impl;//package com.github.anicmv.Inline.impl;

import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.contant.XpEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description xp
 */
@Component
class XpQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_2;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        String imageUrl = "https://jpg.moe/i/3eh09458.jpeg";

        // 先获取所有枚举实例对应的按钮
        List<InlineKeyboardButton> allButtons = Arrays.stream(XpEnum.values())
                //.filter(xp -> !xp.getCallback().equals(XpEnum.XP_DEFAULT.getCallback()))
                .map(xp -> InlineKeyboardButton.builder()
                        .text(xp.getDescription())
                        .callbackData(xp.getCallback())
                        .build())
                .collect(Collectors.toList());

        // 将按钮按每行最多 3 个分组
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < allButtons.size(); i += 3) {
            InlineKeyboardRow row = new InlineKeyboardRow();
            // i 到 i+3 的按钮
            for (int j = i; j < Math.min(i + 3, allButtons.size()); j++) {
                row.add(allButtons.get(j));
            }
            keyboard.add(row);
        }

        // 构建最终的内联键盘
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();


        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("XP")
                .replyMarkup(markup)
                .build();
    }


}
