//package com.github.anicmv.callback.impl;
//
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.github.anicmv.callback.CallbackQueryProvider;
//import com.github.anicmv.entity.FaDian;
//import com.github.anicmv.mapper.FaDianMapper;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
//import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
//
//import java.util.Optional;
//
//
///**
// * @author anicmv
// * @date 2025/3/30 00:48
// * @description 随机图片
// */
//@Component
//public class FaDianCallbackQueryProvider implements CallbackQueryProvider {
//
//    @Resource
//    private FaDianMapper faDianMapper;
//
//    @Override
//    public boolean supports(CallbackQuery callbackQuery) {
//        return "FA_DIAN".equals(callbackQuery.getData());
//    }
//
//    @Override
//    public Optional<PartialBotApiMethod<?>> handle(Update update) {
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//        FaDian faDian = faDianMapper.selectOne(
//                new LambdaQueryWrapper<FaDian>().last("ORDER BY RAND() LIMIT 1")
//        );
//
//        String text = (faDian != null && faDian.getContent() != null)
//                ? faDian.getContent(): "没有找到吐槽内容哦！";
//        InputTextMessageContent content = InputTextMessageContent.builder()
//                .messageText(text)
//                .build();
//
//        // 构建新的 InputMediaPhoto ：指定随机图片及说明文字
//        InputMediaPhoto inputMediaPhoto = InputMediaPhoto.builder()
//                .caption(StrUtil.replace(text, "{holder}", ""))
//                .build();
//
//        EditMessageMedia editMessageMedia;
//        if (callbackQuery.getMessage() != null) {
//            Long chatId = callbackQuery.getMessage().getChatId();
//            Integer messageId = callbackQuery.getMessage().getMessageId();
//            editMessageMedia = EditMessageMedia.builder()
//                    .chatId(chatId)
//                    .messageId(messageId)
//                    .media(inputMediaPhoto)
//                    .build();
//        } else if (callbackQuery.getInlineMessageId() != null) {
//            String inlineMessageId = callbackQuery.getInlineMessageId();
//            editMessageMedia = EditMessageMedia.builder()
//                    .inlineMessageId(inlineMessageId)
//                    .media(inputMediaPhoto)
//                    .build();
//        } else {
//            return Optional.empty();
//        }
//
//        return Optional.of(editMessageMedia);
//    }
//
//}
