//package com.github.anicmv.chose.impl;
//
//import com.github.anicmv.chose.ChosenInlineQueryProvider;
//import com.github.anicmv.contant.BotConstant;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
//import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
//
//import java.util.Optional;
//
///**
// * @author anicmv
// * @date 2025/3/30 00:48
// * @description 发癫编辑
// */
//@Component
//public class FaDianChosenInlineQueryProvider implements ChosenInlineQueryProvider {
//
//    @Override
//    public boolean supports(ChosenInlineQuery chosenInlineQuery) {
//        // 内联菜单的id
//        return BotConstant.N_6.equals(chosenInlineQuery.getResultId());
//    }
//
//    @Override
//    public Optional<PartialBotApiMethod<?>> handle(ChosenInlineQuery chosenInlineQuery) {
//        String inlineMessageId = chosenInlineQuery.getInlineMessageId();
//
//        if (inlineMessageId != null && !inlineMessageId.isEmpty()) {
//            // 构造新的媒体消息，caption
//            InputMediaPhoto inputMediaPhoto = InputMediaPhoto.builder()
//                    .caption("编辑后的消息：这是新的说明文字")
//                    .build();
//
//            EditMessageMedia editMessageMedia = EditMessageMedia.builder()
//                    .inlineMessageId(inlineMessageId)
//                    .media(inputMediaPhoto)
//                    .build();
//
//            // 返回即将执行的编辑方法
//            return Optional.of(editMessageMedia);
//        }
//        return Optional.empty();
//    }
//}
