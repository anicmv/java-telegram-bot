package com.github.anicmv.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author anicmv
 * @date 2025/3/30 01:14
 * @description telegram bot config
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotConfig {
    private String token;
    private String channelId;
}
