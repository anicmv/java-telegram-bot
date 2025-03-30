package com.github.anicmv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

    private final BotConfig botConfig;

    @Autowired
    public TelegramConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public TelegramClient telegramClient() {
        // 通过 BotConfig 获取 token，实例化实现类
        return new OkHttpTelegramClient(botConfig.getToken());
    }
}