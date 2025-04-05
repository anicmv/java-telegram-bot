package com.github.anicmv.Inline.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 编程语言排行榜
 */
@Slf4j
@Component
public class BiliTimelineResultProvider implements InlineQueryResultProvider {

    @Override
    public String getSortId() {
        return BotConstant.N_9;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {
        String text = "食用:关键词'gm/rm' -> 点击哔哩哔哩每日放送";
        String query = inlineQuery.getQuery();

        String imageUrl = "https://jpg.moe/i/hr58gxep.jpeg";
        InputTextMessageContent.InputTextMessageContentBuilder<?, ?> builder = InputTextMessageContent.builder();

        if (StrUtil.isEmpty(query)) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("哔哩哔哩每日放送")
                    .description("食用:关键词'gm/rm' -> 点击哔哩哔哩每日放送")
                    .inputMessageContent(builder.build())
                    .build();
        }

        if (!("gm".equals(query) || "rm".equals(query))) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("哔哩哔哩每日放送")
                    .description("食用:关键词'gm/rm' -> 点击哔哩哔哩每日放送")
                    .inputMessageContent(builder.build())
                    .build();
        }
        String gm = "https://api.bilibili.com/pgc/web/timeline?types=4&before=0&after=0";
        String rm = "https://api.bilibili.com/pgc/web/timeline?types=1&before=0&after=0";

        String result;
        if ("gm".equals(query)) {
            result = timeline(gm);
        } else {
            result = timeline(rm);
        }
        InputTextMessageContent content = InputTextMessageContent.builder()
                .messageText("<pre>" + result + "</pre>")
                .parseMode("HTML")
                .build();


        return InlineQueryResultArticle.builder()
                .id(getSortId())
                .title("哔哩哔哩每日放送")
                .thumbnailUrl("https://jpg.moe/i/hr58gxep.jpeg")
                .inputMessageContent(content)
                .build();
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
