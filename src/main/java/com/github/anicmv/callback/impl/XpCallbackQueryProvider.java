package com.github.anicmv.callback.impl;

import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.contant.XpEnum;
import com.github.anicmv.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;


/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 随机图片
 */
@Slf4j
@Component
public class XpCallbackQueryProvider implements CallbackQueryProvider {

    @Override
    public boolean supports(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("XP_");
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        // 处理逻辑
        InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder = InputMediaPhoto.builder();
        String xp = callbackQuery.getData();
        InputMediaPhoto inputMediaPhoto = getInputMediaPhoto(builder, client, config, xp);
        return BotUtil.getOptionalEditMessageMedia(callbackQuery, inputMediaPhoto, xp);
    }

    private InputMediaPhoto getInputMediaPhoto(InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder, TelegramClient client, BotConfig config, String xp) throws TelegramApiException {
        if (XpEnum.XP_BS.getCallback().equals(xp) || XpEnum.XP_DEFAULT.getCallback().equals(xp)) {
            return builder.media(getRedirectImageUrl(client, config, xp)).build();
        }
        return builder.media(Objects.requireNonNull(getImage(client, config, xp))).build();
    }

    private String getRedirectImageUrl(TelegramClient client, BotConfig config, String xp) throws TelegramApiException {
        String[] directUrl = switch (xp) {
            case BotConstant.CALLBACK_XP_BS -> new String[]{"https://acg.suyanw.cn/whitesilk/random.php",};
            default -> new String[]{"https://acg.suyanw.cn/meizi/random.php",};
        };

        String url = directUrl[0];
        return BotUtil.getTelegramFileId(url, client, config);
    }

    /**
     * 返回图片二进制
     */
    private String getImage(TelegramClient client, BotConfig config, String xp) throws TelegramApiException {
        String[] directUrl = switch (xp) {
            case BotConstant.CALLBACK_XP_JK -> new String[]{"https://api.suyanw.cn/api/jk/", "https://jpg.moe/xp/jk",};
            case BotConstant.CALLBACK_XP_HS -> new String[]{"https://api.suyanw.cn/api/hs/",};
            case BotConstant.CALLBACK_XP_TWIN_TAIL -> new String[]{"https://jpg.moe/xp/双马尾",};
            default -> new String[]{"https://jpg.moe/xp/girl",};
        };

        try (InputStream inputStream = BotUtil.randomImageInputStream(directUrl)) {
            return BotUtil.getTelegramFileId(inputStream, client, config);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
