package com.github.anicmv.chose.impl;

import com.github.anicmv.chose.ChosenInlineQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 三次元随机图片
 */
@Log4j2
@Component
public class RandomScyImageChosenInlineQueryProvider implements ChosenInlineQueryProvider {

    @Override
    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
        // 内联菜单的id
        return BotConstant.N_2.equals(chosenInlineQuery.getResultId());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder = InputMediaPhoto.builder();
        InputMediaPhoto inputMediaPhoto = getInputMediaPhoto(builder, client, config);

        return BotUtil.getOptionalEditMessageMedia(update.getChosenInlineQuery(), inputMediaPhoto);
    }


    private InputMediaPhoto getInputMediaPhoto(InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder, TelegramClient client, BotConfig config) throws TelegramApiException {
        Random random = new Random();
        int randomNum = random.nextInt(100);

        if (randomNum % 2 == 1) {
            // 奇数：随机返回数组中的直链
            return builder.media(getRedirectImageUrl(client, config)).build();
        } else {
            // 偶数：获取通过重定向返回的图片链接
            return builder.media(Objects.requireNonNull(getImage(client, config))).build();
        }
    }


    // 返回一个随机图片的 URL
    private String getRedirectImageUrl(TelegramClient client, BotConfig config) throws TelegramApiException {
        String[] images = new String[]{
                "https://acg.suyanw.cn/meizi/random.php",
                "https://acg.suyanw.cn/whitesilk/random.php",
        };

        String url = BotUtil.randomUrl(images);
        return BotUtil.getTelegramFileId(url, client, config);
    }


    /**
     * 返回图片二进制
     */
    private String getImage(TelegramClient client, BotConfig config) throws TelegramApiException {
        String[] directUrl = new String[]{
                "https://api.suyanw.cn/api/jk/",
                "https://api.suyanw.cn/api/hs/",
        };

        try (InputStream inputStream = BotUtil.randomImageInputStream(directUrl)) {
            return BotUtil.getTelegramFileId(inputStream, client, config);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
