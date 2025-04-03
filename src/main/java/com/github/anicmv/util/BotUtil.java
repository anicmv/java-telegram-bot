package com.github.anicmv.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.config.BotConfig;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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


    public static Optional<PartialBotApiMethod<?>> getOptionalEditMessageMedia(ChosenInlineQuery chosenInlineQuery, InputMediaPhoto inputMediaPhoto) {
        String inlineMessageId = chosenInlineQuery.getInlineMessageId();
        EditMessageMedia editMessageMedia = EditMessageMedia.builder()
                .inlineMessageId(inlineMessageId)
                .media(inputMediaPhoto)
                .build();
        return Optional.of(editMessageMedia);
    }

    public static String getTelegramFileId(String urlOrPath, TelegramClient client, BotConfig config) throws TelegramApiException {
        SendPhoto.SendPhotoBuilder<?, ?> sendPhotoBuilder = SendPhoto.builder().chatId(config.getChannelId());
        if (urlOrPath.startsWith("http")) {
            sendPhotoBuilder.photo(new InputFile(urlOrPath));
        } else {
            sendPhotoBuilder.photo(new InputFile(new File(urlOrPath)));
        }
        SendPhoto sendPhoto = sendPhotoBuilder.build();
        Message message = client.execute(sendPhoto);
        return getTelegramFileId(client, sendPhoto, message, config);
    }

    private static String getTelegramFileId(TelegramClient client, SendPhoto sendPhoto, Message message, BotConfig config) throws TelegramApiException {
        Optional<PhotoSize> photoSizeOptional = message.getPhoto().stream().max(Comparator.comparing(PhotoSize::getFileSize));
        String fileId = photoSizeOptional.map(PhotoSize::getFileId).orElse(null);
        if (fileId != null) {
            client.execute(SendMessage.builder().chatId(config.getChannelId()).text(fileId).build());
        }
        return photoSizeOptional.map(PhotoSize::getFileId).orElse(null);
    }

    public static String getTelegramFileId(InputStream inputStream, TelegramClient client, BotConfig config) throws TelegramApiException {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(config.getChannelId())
                .photo(new InputFile(inputStream, UUID.randomUUID().toString()))
                .build();

        Message message = client.execute(sendPhoto);
        return getTelegramFileId(client, sendPhoto, message, config);
    }
}
