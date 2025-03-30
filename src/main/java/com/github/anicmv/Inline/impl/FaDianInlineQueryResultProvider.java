package com.github.anicmv.Inline.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.anicmv.Inline.InlineQueryResultProvider;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.entity.FaDian;
import com.github.anicmv.mapper.FaDianMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;

import java.util.List;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 2.文章示例
 */
@Component
public class FaDianInlineQueryResultProvider implements InlineQueryResultProvider {

    @Resource
    private FaDianMapper faDianMapper;

    @Override
    public String getSortId() {
        return BotConstant.N_1;
    }

    @Override
    public InlineQueryResult createResult(InlineQuery inlineQuery) {
        String text = "食用:fd 昵称";
        String imageUrl = "https://jpg.moe/i/6kn76jj7.webp";
        String query = inlineQuery.getQuery();

        InputTextMessageContent.InputTextMessageContentBuilder<?, ?> builder = InputTextMessageContent.builder();

        if (StrUtil.isEmpty(query)) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("发癫")
                    .inputMessageContent(builder.build())
                    .build();
        }
        List<String> parts = StrUtil.split(query, ' ', 2);

        if (parts.size() != 2 || !"fd".equals(parts.getFirst())) {
            builder.messageText(text);
            return InlineQueryResultPhoto.builder()
                    .id(getSortId())
                    .photoUrl(imageUrl)
                    .thumbnailUrl(imageUrl)
                    .title("发癫")
                    .inputMessageContent(builder.build())
                    .build();
        }

        String name = parts.getLast();
        FaDian faDian = faDianMapper.selectOne(
                new LambdaQueryWrapper<FaDian>().last("ORDER BY RAND() LIMIT 1")
        );

        text = StrUtil.replace(faDian.getContent(), "{holder}", name);

        builder.messageText(text);

        return InlineQueryResultPhoto.builder()
                .id(getSortId())
                .photoUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .title("发癫")
                .inputMessageContent(builder.build())
                .build();

    }
}
