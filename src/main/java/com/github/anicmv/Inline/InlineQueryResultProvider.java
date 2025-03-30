package com.github.anicmv.Inline;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 内联查询提供者
 */
public interface InlineQueryResultProvider {

    /**
     * 获取用于排序的 id，此 id 将传递给 InlineQueryResult 的 id 字段
     *
     * @return 排序 id
     */
    String getSortId();

    /**
     * 根据 inline query 请求创建 InlineQueryResult 结果
     */
    InlineQueryResult createResult(InlineQuery inlineQuery);
}