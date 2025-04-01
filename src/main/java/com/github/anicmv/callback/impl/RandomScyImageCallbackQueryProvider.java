package com.github.anicmv.callback.impl;

import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.InputStream;
import java.util.Optional;
import java.util.Random;


/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 随机图片
 */
@Slf4j
@Component
public class RandomScyImageCallbackQueryProvider implements CallbackQueryProvider {

    @Override
    public boolean supports(CallbackQuery callbackQuery) {
        return BotConstant.CALLBACK_RANDOM_SCY.equals(callbackQuery.getData());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update) {

        InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder = InputMediaPhoto.builder();
        InputMediaPhoto inputMediaPhoto = getEditMessageMedia(builder);

        return BotUtil.getOptionalEditMessageMedia(update.getCallbackQuery(), inputMediaPhoto);
    }


    private InputMediaPhoto getEditMessageMedia(InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder) {
        Random random = new Random();
        int randomNum = random.nextInt(100);

        if (randomNum % 2 == 1) {
            // 奇数：随机返回数组中的直链
            return builder.media(getRedirectImageUrl()).build();
        } else {
            // 偶数：获取通过重定向返回的图片链接
            return builder.media(getImage(), "scy.jpg").build();
        }
    }


    // 返回一个随机图片的 URL
    private String getRedirectImageUrl() {
        String[] images = new String[]{
                "https://acg.suyanw.cn/meizi/random.php",
                "https://acg.suyanw.cn/whitesilk/random.php",
        };

        return BotUtil.randomUrl(images);
    }


    /**
     * 返回图片二进制
     */
    private InputStream getImage() {
        String[] directUrl = new String[]{
                "https://api.suyanw.cn/api/jk/",
                "https://api.suyanw.cn/api/hs/",
        };

        return BotUtil.randomImageInputStream(directUrl);
    }


}
