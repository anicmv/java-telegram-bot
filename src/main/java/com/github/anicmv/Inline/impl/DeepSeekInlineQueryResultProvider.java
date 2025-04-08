package com.github.anicmv.Inline.impl;

import cn.hutool.core.util.StrUtil;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
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
 * @description 2.文章示例
 */
@Component
public class DeepSeekInlineQueryResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_3;
    }

    @Override
    public InlineQueryResult createResult(Update update, TelegramClient client, BotConfig config) {
        InlineQuery inlineQuery = update.getInlineQuery();
        String text = "食用:关键词'ds' + 空格 + prompt -> 点击DeepSeek";
        String imageUrl = "https://jpg.moe/i/bx9jrubq.png";
        String query = inlineQuery.getQuery();

        InputTextMessageContent.InputTextMessageContentBuilder<?, ?> builder = InputTextMessageContent.builder();

        if (StrUtil.isEmpty(query)) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("DeepSeek")
                    .description("食用:关键词'ds' + 空格 + prompt -> 点击DeepSeek")
                    .inputMessageContent(builder.build())
                    .build();
        }
        List<String> parts = StrUtil.split(query, ' ', 2);

        if (parts.size() != 2 || !"ds".equals(parts.getFirst())) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("DeepSeek")
                    .description("食用:关键词'ds' + 空格 + prompt -> 点击DeepSeek")
                    .inputMessageContent(builder.build())
                    .build();
        }


        // 同时附加内联键盘按钮实现交互
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("thinking...")
                .callbackData(BotConstant.CALLBACK_DEEP_SEEK)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button);
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText(parts.get(1))
                .build();

        return InlineQueryResultArticle.builder()
                .id(getSortId())
                .inputMessageContent(content)
                .description("食用:关键词'ds'+空格+昵称 -> 点击DeepSeek")
                .thumbnailUrl(imageUrl)
                .title("DeepSeek")
                .replyMarkup(markup)
                .build();

    }
}
