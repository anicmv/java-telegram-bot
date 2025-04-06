package com.github.anicmv.callback.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.callback.CallbackQueryProvider;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 随机图片
 */
@Slf4j
@Component
public class BiliTimelineCallbackQueryProvider implements CallbackQueryProvider {

    @Override
    public boolean supports(CallbackQuery callbackQuery) {
        return BotConstant.CALLBACK_BILI_GM.equals(callbackQuery.getData())
                || BotConstant.CALLBACK_BILI_RM.equals(callbackQuery.getData());
    }

    @Override
    public Optional<PartialBotApiMethod<?>> handle(Update update, TelegramClient client, BotConfig config) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        // 处理逻辑
        String timelineCallbackData = callbackQuery.getData();
        String gm = "https://api.bilibili.com/pgc/web/timeline?types=4&before=0&after=0";
        String rm = "https://api.bilibili.com/pgc/web/timeline?types=1&before=0&after=0";

        String content;
        if (BotConstant.CALLBACK_BILI_GM.equals(timelineCallbackData)) {
            content = timeline(gm);
        } else {
            content = timeline(rm);
        }

        return getOptionalEditMessageText(callbackQuery, content);
    }

    public static Optional<PartialBotApiMethod<?>> getOptionalEditMessageText(CallbackQuery callbackQuery, String content) {
        String inlineMessageId = callbackQuery.getInlineMessageId();
        EditMessageText editMessageText = EditMessageText.builder()
                .inlineMessageId(inlineMessageId)
                .text("<pre>" + content + "</pre>")
                .parseMode("HTML")
                .build();
        return Optional.of(editMessageText);
    }

    private String timeline(String api) {
        String s = HttpUtil.get(api, Map.of());
        JSONObject biliJson = JSONUtil.parseObj(s);
        Integer code = (Integer) biliJson.get("code");
        if (code.compareTo(0) != 0) {
            return null;
        }
        JSONArray parsedArray = JSONUtil.parseArray(biliJson.get("result"));
        if (parsedArray == null || parsedArray.isEmpty()) {
            return null;
        }
        JSONObject episodes = JSONUtil.parseObj(parsedArray.getFirst());
        JSONArray episodesArray = JSONUtil.parseArray(episodes.get("episodes"));

        // 定义表头
        String headerTime = "Time";
        String headerTitle = "Title";

        // 初始赋值为表头的长度
        int maxTimeLen = headerTime.length();
        int maxTitleLen = headerTitle.length();

        // 存储所有行数据
        List<String[]> rows = new ArrayList<>();
        for (Object obj : episodesArray) {
            JSONObject episode = JSONUtil.parseObj(obj);
            String time = episode.getStr("pub_time");
            String title = episode.getStr("title");
            title = title + " -> (" + episode.getStr("pub_index") + ")";
            maxTimeLen = Math.max(maxTimeLen, time.length());
            maxTitleLen = Math.max(maxTitleLen, title.length());
            rows.add(new String[]{time, title});
        }

        // 为了更美观，给每列左右补充2个空格（可以根据需要调整）
        maxTimeLen += 2;
        maxTitleLen += 2;

        // 构造格式化模板字符串
        String format = "%-" + maxTimeLen + "s  %-" + maxTitleLen + "s%n";

        // 封装输出结果
        StringBuilder builder = new StringBuilder();
        // 表头
        builder.append(String.format(format, headerTime, headerTitle));
        // 分隔线
        builder.append("-".repeat(maxTimeLen + 2 + maxTitleLen)).append("\n");
        // 数据行
        for (String[] arr : rows) {
            builder.append(String.format(format, arr[0], arr[1]));
        }

        // 最后输出整个结果
        return builder.toString();
    }

}
