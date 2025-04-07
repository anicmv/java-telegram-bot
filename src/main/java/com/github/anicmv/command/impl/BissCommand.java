package com.github.anicmv.command.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.anicmv.command.BotCommand;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.entity.Biss;
import com.github.anicmv.mapper.BissMapper;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /biss
 */
@Component
public class BissCommand implements BotCommand {

    @Resource
    private BotConfig config;

    @Resource
    private BissMapper bissMapper;

    @Override
    public boolean supports(String commandText) {
        return BotUtil.isThisCommand(BotConstant.BISS, commandText.trim(), config);
    }

    @Override
    public SendMessage execute(Update update) {
        long chatId = update.getMessage().getChatId();
        Biss biss = bissMapper.selectOne(
                new LambdaQueryWrapper<Biss>().last("ORDER BY RAND() LIMIT 1")
        );

        String text = (biss != null && biss.getContent() != null)
                ? biss.getContent()
                : "没有找到吐槽内容哦！";
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}