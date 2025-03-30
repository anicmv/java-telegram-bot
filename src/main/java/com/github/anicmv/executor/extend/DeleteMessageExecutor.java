package com.github.anicmv.executor.extend;

import com.github.anicmv.executor.MessageOperationExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author anicmv
 * @date 2025/3/30 17:08
 * @description 删除 /ping 的用户消息
 */
@Log4j2
@Component
public class DeleteMessageExecutor extends MessageOperationExecutor {

    public void execute(Long chatId, Integer messageId) {
        // 构造 DeleteMessage 对象
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), messageId);
        try {
            super.execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("删除消息失败", e);
        }
    }
}
