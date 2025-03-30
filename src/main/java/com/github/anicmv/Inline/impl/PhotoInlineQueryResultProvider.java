//package com.github.anicmv.Inline.impl;
//
//import com.github.anicmv.Inline.InlineQueryResultProvider;
//import com.github.anicmv.contant.BotConstant;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
//import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author anicmv
// * @date 2025/3/30 00:48
// * @description 1.随机图片
// */
//@Component
//public class PhotoInlineQueryResultProvider implements InlineQueryResultProvider {
//
//    @Override
//    public String getSortId() {
//        return BotConstant.N_6;
//    }
//
//    @Override
//    public InlineQueryResult createResult(InlineQuery inlineQuery) {
//        // 此处为了示例，直接构造固定的图片展示，实际可以传入动态数据
//        // 同时附加内联键盘按钮实现交互
//        InlineKeyboardButton button = InlineKeyboardButton.builder()
//                .text("随机图片")
//                .callbackData("RANDOM_IMAGE")
//                .build();
//
//        InlineKeyboardRow row = new InlineKeyboardRow();
//        row.add(button);
//        List<InlineKeyboardRow> keyboard = new ArrayList<>();
//        keyboard.add(row);
//
//        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
//                .keyboard(keyboard)
//                .build();
//
//        return InlineQueryResultPhoto.builder()
//                .id(getSortId())
//                .photoUrl("https://img3.doubanio.com/view/photo/l/public/p2917594343.webp")
//                .thumbnailUrl("https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2917594343.webp")
//                .title("示例图片")
//                .description("点击后发送图片")
//                .replyMarkup(markup)
//                .build();
//        // 构造 InlineQueryResultPhoto 时直接设置图片和描述，不附加 inline keyboard
//        //return InlineQueryResultPhoto.builder()
//        //        .id("1")
//        //        .photoUrl(photoUrl)
//        //        .thumbnailUrl(thumbUrl)
//        //        .caption("亲爱的{昵称}\n今天的老婆是 蒂安娜·兰斯特!")
//        //        .build();
//    }
//
//
//}
