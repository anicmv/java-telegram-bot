package com.github.anicmv.chose.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.chose.ChosenInlineQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import com.github.anicmv.util.HttpUtil;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 二次元随机图片
 */
@Log4j2
@Component
public class RandomEcyImageChosenInlineQueryProvider implements ChosenInlineQueryProvider {

    @Override
    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
        // 内联菜单的id
        return BotConstant.N_1.equals(chosenInlineQuery.getResultId());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder = InputMediaPhoto.builder();
        InputMediaPhoto inputMediaPhoto = getEditMessageMedia(builder, client, config);

        return BotUtil.getOptionalEditMessageMedia(update.getChosenInlineQuery(), inputMediaPhoto);
    }

    private InputMediaPhoto getEditMessageMedia(InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder, TelegramClient client, BotConfig config) throws TelegramApiException {
        Random random = new Random();
        // 随机生成 0, 1, 2 三个数中的一个
        int choice = random.nextInt(3);

        return switch (choice) {
            case 0 ->
                // 情况0：返回重定向图片URL
                    builder.media(getRedirectImageUrl(client, config)).build();
            case 1 ->
                // 情况1：返回通过 getImage() 获取的图片，并设置文件名 "ecy.jpg"
                    builder.media(Objects.requireNonNull(getImage(client, config))).build();
            case 2 ->
                // 情况2：返回 piXivUrl 图片
                    builder.media(Objects.requireNonNull(getImage(client, config))).build();
            default ->
                // 默认情况，这里其实不会走到
                    builder.media(getRedirectImageUrl(client, config)).build();
        };
    }


    /**
     * 返回图片二进制
     */
    private String getImage(TelegramClient client, BotConfig config) throws TelegramApiException {
        String[] directUrl = new String[]{
                "https://api.sretna.cn/api/pc.php",
                "https://api.sretna.cn/api/pe.php",
                "https://api.lqbby.com/api/dm",
        };

        try (InputStream inputStream = BotUtil.randomImageInputStream(directUrl)) {
            return BotUtil.getTelegramFileId(inputStream, client, config);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    // 返回一个随机图片的 URL
    private String getRedirectImageUrl(TelegramClient client, BotConfig config) throws TelegramApiException {
        // 随机二次元头像 https://www.loliapi.com/acg/pp/
        String[] images = new String[]{
                "https://moe.jitsu.top/img/",
                "https://www.loliapi.com/bg/",
        };
        String url = BotUtil.randomUrl(images);
        return BotUtil.getTelegramFileId(url, client, config);
    }


    private String getPiXivUrl() {
        String api = "https://api.mossia.top/randPic/pixiv";
        String dataJson = HttpUtil.get(api, Map.of(BotConstant.UA, BotConstant.USER_AGENT));
        JSONObject data = JSONUtil.parseObj(dataJson);
        return data.getStr("data");
    }


}
