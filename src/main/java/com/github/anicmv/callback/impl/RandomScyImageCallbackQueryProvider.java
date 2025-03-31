package com.github.anicmv.callback.impl;

import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.Map;
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
        CallbackQuery callbackQuery = update.getCallbackQuery();
        InputMediaPhoto inputMediaPhoto = InputMediaPhoto.builder()
                .media(getImageUrl())
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

    private String getImageUrl() {
        String[] directUrl = new String[] {
                "https://acg.suyanw.cn/pcmv/",
                "https://acg.suyanw.cn/sjmv/random.php",
        };

        Random random = new Random();
        // 产生一个随机数，这里选取 0 到 99 的区间
        int randomNum = random.nextInt(100);

        if (randomNum % 2 == 1) {
            // 如果随机数为奇数，从直链数组中随机选取一个
            int index = random.nextInt(directUrl.length);
            return directUrl[index];
        } else {
            // 如果随机数为偶数，返回重定向的图片链接
            return getRedirectImageUrl();
        }
    }

    // 返回一个随机图片的 URL
    private String getRedirectImageUrl() {
        String randomApi = getApi();
        String redirectUrl = HttpUtil.redirectUrl(randomApi, Map.of());
        if (redirectUrl == null) {
            log.error("Error occurred during redirect URL");
        }
        return redirectUrl;
    }


    private String getApi() {
        // 随机二次元头像 https://www.loliapi.com/acg/pp/
        String[] images = new String[]{
                "https://acg.suyanw.cn/meizi/random.php",
                "https://acg.suyanw.cn/sjmv/random.php",
                "https://acg.suyanw.cn/jk/random.php",
                "https://acg.suyanw.cn/whitesilk/random.php",
                "https://acg.suyanw.cn/hs/random.php",
        };
        int idx = (int) (Math.random() * images.length);
        return images[idx];
    }
}
