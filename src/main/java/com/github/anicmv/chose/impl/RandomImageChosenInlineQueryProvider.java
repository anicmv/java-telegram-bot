package com.github.anicmv.chose.impl;

import com.github.anicmv.chose.ChosenInlineQueryProvider;
import com.github.anicmv.contant.BotConstant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 随机图片
 */
@Component
public class RandomImageChosenInlineQueryProvider implements ChosenInlineQueryProvider {

    @Override
    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
        // 内联菜单的id
        return BotConstant.N_3.equals(chosenInlineQuery.getResultId());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(ChosenInlineQuery chosenInlineQuery) {
        String inlineMessageId = chosenInlineQuery.getInlineMessageId();
        if (inlineMessageId == null || inlineMessageId.isEmpty()) {
            return Optional.empty();
        }
        InputMediaPhoto inputMediaPhoto = InputMediaPhoto.builder()
                .media(getRandomImageUrl())
                .caption("亲爱的{昵称}\n今天的老婆是 蒂安娜·兰斯特!")
                .build();

        EditMessageMedia editMessageMedia = EditMessageMedia.builder()
                .inlineMessageId(inlineMessageId)
                .media(inputMediaPhoto)
                .build();

        return Optional.of(editMessageMedia);
    }

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
