package com.github.anicmv.command.impl;

import com.github.anicmv.command.BotCommand;
import com.github.anicmv.config.BotConfig;
import com.github.anicmv.contant.BotConstant;
import com.github.anicmv.mapper.DissMapper;
import com.github.anicmv.util.BotUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description /diss
 */
@Component
public class DissCommand implements BotCommand {

    @Resource
    private BotConfig config;

    @Resource
    private DissMapper dissMapper;

    @Override
    public boolean supports(String commandText) {
        return BotUtil.isThisCommand(BotConstant.DISS, commandText.trim(), config);
    }

    @Override
    public SendMessage execute(Update update) {
        return BotUtil.buildSendMessage(update, config, dissMapper, null);
    }
}