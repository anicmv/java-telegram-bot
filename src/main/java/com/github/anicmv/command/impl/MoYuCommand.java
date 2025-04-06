package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.InputStream;
import java.util.Map;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /moyu
 */
@Log4j2
@Component
public class MoYuCommand implements BotCommand {

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.MO_YU);
    }

    @Override
    public SendPhoto execute(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();

        InputStream photo = HttpUtil.getInputStream(getImageUrl(), Map.of());
        if (photo == null) {
            return SendPhoto.builder()
                    .chatId(chatId)
                    .caption("获取图片失败，请稍后重试。")
                    .build();
        }

        return SendPhoto.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .photo(new InputFile(photo, "moyu.jpg"))
                .build();
    }

    /**
     * 调用外部 API 获取经过重定向后的图片 URL
     */
    private String getImageUrl() {
        String moYuApi = "https://api.vvhan.com/api/moyu";
        String redirectUrl = HttpUtil.redirectUrl(moYuApi, Map.of());
        if (redirectUrl == null) {
            log.error("Error occurred during redirect URL");
        }
        return redirectUrl;
    }
}