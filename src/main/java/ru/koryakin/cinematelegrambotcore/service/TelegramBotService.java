package ru.koryakin.cinematelegrambotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.koryakin.cinematelegrambotcore.config.BotConfig;
import ru.koryakin.cinematelegrambotcore.utils.JsonParser;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    BotConfig config;
    @Autowired
    JsonParser parser;


    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/init":
                    parser.updateDb();
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }

    private void startCommand(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
        log.info("Ответ пользователю: {}", name);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка Telegram API: {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
       return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
