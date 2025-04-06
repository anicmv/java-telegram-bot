package com.github.anicmv.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.XpEnum;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    // 辅助方法：将字符串中的 MarkdownV2 特殊字符转义
    public static String escapeMarkdownV2(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }

    public static String removeMarkdownV2(String text) {
        return text.replace("\\", "")
                .replace("_", "")
                .replace("*", "")
                .replace("[", "")
                .replace("]", "")
                .replace("(", "")
                .replace(")", "")
                .replace("~", "")
                .replace("`", "")
                .replace(">", "")
                .replace("#", "")
                .replace("+", "")
                .replace("-", "")
                .replace("=", "")
                .replace("|", "")
                .replace("{", "")
                .replace("}", "")
                .replace(".", "")
                .replace("!", "");
    }



    public static Optional<PartialBotApiMethod<?>> getOptionalEditMessageMedia(CallbackQuery callbackQuery, InputMediaPhoto inputMediaPhoto, String xp) {
        String inlineMessageId = callbackQuery.getInlineMessageId();
        String fullName = callbackQuery.getFrom().getFirstName() +
                (callbackQuery.getFrom().getLastName() == null ? "" : callbackQuery.getFrom().getLastName());
        // 将全名转义（MarkdownV2 格式）
        String escapedFullName = escapeMarkdownV2(fullName);
        String clickableUsername = "[" + escapedFullName + "](tg://user?id=" + callbackQuery.getFrom().getId() + ")";

        // 其余文本同样需要转义（如果有特殊字符）
        String extraText = " 的xp是: " + XpEnum.fromCallback(xp).getDescription();
        // 如果extraText中可能包含特殊字符，也应调用 escapeMarkdownV2(extraText)
        String escapedExtraText = escapeMarkdownV2(extraText);

        String caption = clickableUsername + escapedExtraText;
        InputMediaPhoto inputMediaPhotoWithCaption = InputMediaPhoto.builder()
                .media(inputMediaPhoto.getMedia())
                .caption(caption)
                .parseMode("Markdown")
                .build();

        EditMessageMedia editMessageMedia = EditMessageMedia.builder()
                .inlineMessageId(inlineMessageId)
                .media(inputMediaPhotoWithCaption)
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
        return getTelegramFileId(client, message, config);
    }


    private String resolveRedirectUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // 不自动跟随重定向
        connection.setInstanceFollowRedirects(false);
        int statusCode = connection.getResponseCode();
        // 判断是否重定向
        if (statusCode == HttpURLConnection.HTTP_MOVED_PERM ||
                statusCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
            String redirectUrl = connection.getHeaderField("Location");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return redirectUrl;
            }
        }
        return url;
    }

    private static String getTelegramFileId(TelegramClient client, Message message, BotConfig config) throws TelegramApiException {
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
        return getTelegramFileId(client, message, config);
    }
}
