package com.github.anicmv.callback.impl;

import com.github.anicmv.callback.CallbackQueryProvider;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.Optional;


/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 随机图片
 */
@Component
public class RandomImageCallbackQueryProvider implements CallbackQueryProvider {

    @Override
    public boolean supports(CallbackQuery callbackQuery) {
        return "RANDOM_IMAGE".equals(callbackQuery.getData());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        // 构建新的 InputMediaPhoto ：指定随机图片及说明文字
        InputMediaPhoto inputMediaPhoto = InputMediaPhoto.builder()
                .media(getRandomImageUrl())
                .caption("亲爱的亲爱的{昵称}\n今天的老婆是 蒂安娜·兰斯特!")
                .build();

        EditMessageMedia editMessageMedia;
        if (callbackQuery.getMessage() != null) {
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            editMessageMedia = EditMessageMedia.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .media(inputMediaPhoto)
                    .build();
        } else if (callbackQuery.getInlineMessageId() != null) {
            String inlineMessageId = callbackQuery.getInlineMessageId();
            editMessageMedia = EditMessageMedia.builder()
                    .inlineMessageId(inlineMessageId)
                    .media(inputMediaPhoto)
                    .build();
        } else {
            return Optional.empty();
        }

        return Optional.of(editMessageMedia);
    }

    // 示例方法：返回一个随机图片的 URL
    private String getRandomImageUrl() {
        String[] images = new String[]{
                "https://img9.doubanio.com/view/photo/s_ratio_poster/public/p2919558104.webp",
                "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2918518301.webp",
                "https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2919591337.webp"
        };
        int idx = (int) (Math.random() * images.length);
        return images[idx];
    }
}
