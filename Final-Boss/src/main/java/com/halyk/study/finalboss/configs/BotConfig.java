package com.halyk.study.finalboss.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BotConfig {

    private final String name;

    private final String token;

    public BotConfig(@Value("${telegram.bot.name}") String name,
                     @Value("${telegram.bot.token}") String token) {
        this.name = name;
        this.token = token;
    }
}
