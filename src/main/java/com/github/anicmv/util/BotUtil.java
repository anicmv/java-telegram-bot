package com.github.anicmv.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * @author anicmv
 * @date 2025/3/30 18:18
 * @description 工具类
 */
@Log4j2
public class BotUtil {
    public static String kfc() {
        String response = HttpUtil.get("https://api.shadiao.pro/kfc", Map.of("Referer", "https://kfc.shadiao.pro/"));
        JSONObject responseJson = JSONUtil.parseObj(response);
        return responseJson.getByPath("data.text").toString();
    }

    public static String randomUrl(String[] images) {
        int idx = (int) (Math.random() * images.length);
        String redirectUrl = HttpUtil.redirectUrl(images[idx], Map.of());
        if (redirectUrl == null) {
            log.error("Error occurred during redirect URL");
        }
        return redirectUrl;
    }


    public static InputStream randomImageInputStream(String[] images) {
        int idx = (int) (Math.random() * images.length);
        String api = images[idx];
        return HttpUtil.getInputStream(api, Map.of());
    }


    public static Optional<PartialBotApiMethod<?>> getOptionalEditMessageMedia(CallbackQuery callbackQuery, InputMediaPhoto inputMediaPhoto) {
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
}
