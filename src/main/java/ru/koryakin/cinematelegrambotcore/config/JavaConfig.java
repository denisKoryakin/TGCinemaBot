package ru.koryakin.cinematelegrambotcore.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.koryakin.cinematelegrambotcore.utils.JsonParser;

@Configuration
@EntityScan(basePackages = "ru.koryakin.cinematelegrambotcore.entity")
public class JavaConfig {

    @Bean
    public JsonParser jsonParser() {
        return new JsonParser();
    }
}
