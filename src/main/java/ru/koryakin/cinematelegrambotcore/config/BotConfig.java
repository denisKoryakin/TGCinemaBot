package ru.koryakin.cinematelegrambotcore.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data /* автоматическое создание конструктора, геттера и сеттера (lombok) */
@PropertySource("classpath:application.properties")
//@EntityScan(basePackages = "ru.koryakin.cinematelegrambotcore.entity")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.key}")
    String token;
}
