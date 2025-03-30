package com.github.anicmv.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author anicmv
 * @date 2025/3/30 00:48
 * @description 命令接口
 */
public interface BotCommand {
    /**
     * 判断命令是否匹配输入文本
     */
    boolean supports(String commandText);

    /**
     * 处理命令，并返回需要发送的消息对象
     */
    SendMessage execute(Update update);
}