package ru.koryakin.cinematelegrambotcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import ru.koryakin.cinematelegrambotcore.controller.MovieController;

@SpringBootApplication
public class CinemaTelegramBotCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaTelegramBotCoreApplication.class, args);
    }
}
