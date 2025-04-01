package com.github.anicmv.callback.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.BotUtil;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.InputStream;
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
public class RandomEcyImageCallbackQueryProvider implements CallbackQueryProvider {

    @Override
    public boolean supports(CallbackQuery callbackQuery) {
        return BotConstant.CALLBACK_RANDOM_ECY.equals(callbackQuery.getData());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update) {
        InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder = InputMediaPhoto.builder();
        InputMediaPhoto inputMediaPhoto = getEditMessageMedia(builder);

        return BotUtil.getOptionalEditMessageMedia(update.getCallbackQuery(), inputMediaPhoto);
    }

    private InputMediaPhoto getEditMessageMedia(InputMediaPhoto.InputMediaPhotoBuilder<?, ?> builder) {
        Random random = new Random();
        // 随机生成 0, 1, 2 三个数中的一个
        int choice = random.nextInt(3);

        return switch (choice) {
            case 0 ->
                // 情况0：返回重定向图片URL
                    builder.media(getRedirectImageUrl()).build();
            case 1 ->
                // 情况1：返回通过 getImage() 获取的图片，并设置文件名 "ecy.jpg"
                    builder.media(getImage(), "ecy.jpg").build();
            case 2 ->
                // 情况2：返回 piXivUrl 图片
                    builder.media(getImage(), "ecy.jpg").build();
            default ->
                // 默认情况，这里其实不会走到
                    builder.media(getRedirectImageUrl()).build();
        };
    }


    /**
     * 返回图片二进制
     */
    private InputStream getImage() {
        String[] directUrl = new String[]{
                "https://api.sretna.cn/api/pc.php",
                "https://api.sretna.cn/api/pe.php",
                "https://api.lqbby.com/api/dm",
        };

        return BotUtil.randomImageInputStream(directUrl);
    }


    // 返回一个随机图片的 URL
    private String getRedirectImageUrl() {
        // 随机二次元头像 https://www.loliapi.com/acg/pp/
        String[] images = new String[]{
                "https://moe.jitsu.top/img/",
                "https://www.loliapi.com/bg/",
        };

        return BotUtil.randomUrl(images);
    }


    private String getPiXivUrl() {
        String api = "https://api.mossia.top/randPic/pixiv";
        String dataJson = HttpUtil.get(api, Map.of());
        JSONObject data = JSONUtil.parseObj(dataJson);
        return data.getStr("data");
    }
}
