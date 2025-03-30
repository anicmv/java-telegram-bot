package com.github.anicmv.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * @author anicmv
 * @date 2025/3/30 18:18
 * @description 工具类
 */
public class BotUtil {
    public static String kfc() {
        String response = HttpUtil.get("https://api.shadiao.pro/kfc", Map.of("Referer", "https://kfc.shadiao.pro/"));
        JSONObject responseJson = JSONUtil.parseObj(response);
        return responseJson.getByPath("data.text").toString();
    }
}
