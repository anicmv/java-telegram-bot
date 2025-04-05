package com.github.anicmv.command.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.anicmv.command.BotCommand;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.entity.FaDian;
import com.github.anicmv.mapper.FaDianMapper;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /fd
 */
@Component
public class FaDianCommand implements BotCommand {

    @Resource
    private FaDianMapper faDianMapper;

    @Override
    public boolean supports(String commandText) {
        return commandText.trim().startsWith(BotConstant.FADIAN);
    }

    @Override
    public SendMessage execute(Update update) {
        long chatId = update.getMessage().getChatId();

        Message replyToMessage = update.getMessage().getReplyToMessage();
        if (replyToMessage == null) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("你得回复一个人")
                    .build();
        }

        FaDian faDian = faDianMapper.selectOne(
                new LambdaQueryWrapper<FaDian>().last("ORDER BY RAND() LIMIT 1")
        );

        String text = (faDian != null && faDian.getContent() != null)
                ? faDian.getContent()
                : "没有找到吐槽内容哦！";

        String fullName = replyToMessage.getFrom().getFirstName() +
                (replyToMessage.getFrom().getLastName() == null ? "" : replyToMessage.getFrom().getLastName());
        // 将全名转义（MarkdownV2 格式）
        String escapedFullName = BotUtil.escapeMarkdownV2(fullName);
        String clickableUsername = "[" + escapedFullName + "](tg://user?id=" + replyToMessage.getFrom().getId() + ")";


        return SendMessage.builder()
                .chatId(chatId)
                .text(StrUtil.replace(text, "{holder}", clickableUsername))
                .parseMode("Markdown")
                .build();
    }
}