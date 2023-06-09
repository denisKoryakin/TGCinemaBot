package ru.koryakin.cinematelegrambotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.koryakin.cinematelegrambotcore.config.BotConfig;
import ru.koryakin.cinematelegrambotcore.entity.Movie;
import ru.koryakin.cinematelegrambotcore.entity.User;
import ru.koryakin.cinematelegrambotcore.repository.FavoriteDao;
import ru.koryakin.cinematelegrambotcore.repository.MovieDao;
import ru.koryakin.cinematelegrambotcore.repository.UserDao;
import ru.koryakin.cinematelegrambotcore.utils.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    final BotConfig config;
    @Autowired
    JsonParser jsonParser;
    @Autowired
    UserDao userDao;
    @Autowired
    MovieDao movieDao;
    @Autowired
    FavoriteDao favoriteDao;
    @Autowired
    TelegramKeyboardBuilder telegramKeyboardBuilder;
    @Autowired
    MovieEvaluator evaluator;

    private final static String HELP_TEXT = "Это бот по подбору кино. Управление ботом осуществляется через меню и клавиатуру. " +
            "Для поиска фильма по названию просто отправьте сообщение с его названием." +
            "Для начала работы наберите команду /start - произойдет ваша регистрация в системе." +
            "При регистрации вам становятся доступными следующие функции: добавление фильмов в избранное, возможность оценки фильмов";

    public TelegramBotService(BotConfig config) {
        this.config = config;
        /* инициализация меню */
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "начало работы, и регистрация пользователя"));
        commandList.add(new BotCommand("/mydata", "получить мои метаданные"));
        commandList.add(new BotCommand("/deletemydata", "удалить мои метаданные"));
        commandList.add(new BotCommand("/help", "помощь в работе с ботом"));
//        commandList.add(new BotCommand("/settings", "настройки"));
        /* трансляция меню */
        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка Telegram API: {}", e.getMessage());
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> {
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    registryNewUser(update.getMessage());
                }
                case "/help" -> prepareMessage(chatId, HELP_TEXT);
                case "/mydata" -> showData(update.getMessage());
                case "/deletemydata" -> deleteUser(update.getMessage());
                case "/init" -> jsonParser.updateDb();
                case "Мое избранное" -> showMyFavoriteMovies(chatId);
                case "Мои лайки" -> showMyLikedMovies(chatId);
                // TODO: 01.06.2023 добавить кейсы с постоянной клавиатуры
                default -> searchMovie(update.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "DELETE_DATA_BUTTON" -> {
                    Optional<User> userToDelete = userDao.findById((int) chatId);
                    if (userToDelete.isPresent()) {
                        userDao.deleteById((int) chatId);
                    }
                    String text = "Ваши данные удалены";
                    executeEditMessageText(text, chatId, messageId);
                }
                case "NOT_DELETE_DATA_BUTTON" -> {
                    String text = "Ваши данные остались в базе данных";
                    executeEditMessageText(text, chatId, messageId);
                }
                default -> {
                    String[] words = callbackData.split("_");
                    String movieId = words[words.length - 1];
                    if (words[0].equals("LIKE")) {
                        executeEditMessageText(evaluator.like(chatId, movieId), chatId, messageId);
                    } else if (words[0].equals("DISLIKE")) {
                        executeEditMessageText(evaluator.dislike(chatId, movieId), chatId, messageId);
                    } else if (callbackData.contains("ADD_TO_MY_FAVORITES_BUTTON_")) {
                        executeEditMessageText(evaluator.toFavorite(chatId, movieId), chatId, messageId);
                    } else if (callbackData.contains("DELETE_AT_MY_FAVORITES_BUTTON_")) {
                        executeEditMessageText(evaluator.deleteAtMyFavorite(chatId, movieId), chatId, messageId);
                    }
                }
            }
        }
    }

    private void startCommand(long chatId, String name) {
        String answer = "Привет, " + name + ", с этого момента Вы зарегистрированы в нашей системе подбора фильмов!";
        prepareMessage(chatId, answer);
        log.info("Регистрация пользователя {}", name);
    }

    private void prepareMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(telegramKeyboardBuilder.startKeyboard());
        sendMessage(message);
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка Telegram API: {}", e.getMessage());
        }
    }

    private void sendImage(long chatId, String imgUrl) {
        SendPhoto photo = new SendPhoto();
        try {
            URL url = new URL(imgUrl);
            InputFile file = new InputFile(String.valueOf(url));
            photo.setChatId(String.valueOf(chatId));
            photo.setPhoto(file);
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                log.error("Ошибка Telegram API: {}", e.getMessage());
            }
        } catch (MalformedURLException e) {
            log.warn("ошибка URL: {}", e.getMessage());
        }
    }

    private void registryNewUser(Message message) {
        if (userDao.findById(Math.toIntExact(message.getChatId())).isEmpty()) {
            User newUser = new User();
            Chat chat = message.getChat();

            newUser.setChatId(chat.getId());
            newUser.setFirstName(chat.getFirstName());
            newUser.setLastName(chat.getLastName());
            newUser.setUserName(chat.getUserName());
            newUser.setRegisteredTime(new Timestamp(System.currentTimeMillis()));

            userDao.save(newUser);
        }
    }

    private void showData(Message message) {
        Chat chat = message.getChat();
        int chatId = Math.toIntExact(chat.getId());
        Optional<User> userFind = userDao.findById(chatId);
        if (userFind.isPresent()) {
            prepareMessage(chatId, userFind.get().toString());
        } else {
            prepareMessage(chatId, "Вы не зарегистрированы");
        }
    }

    private void deleteUser(Message message) {
        Chat chat = message.getChat();
        int chatId = Math.toIntExact(chat.getId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Вы действительно хотите удалить свои данные? " +
                "Удаление данных означает сброс предпочтений, что повлияет на подбор фильмов от бота. " +
                "После удаления восстановить их будет не возможно!!");
        sendMessage.setReplyMarkup(telegramKeyboardBuilder.deleteButtons());
        sendMessage(sendMessage);
    }

    private void showMyFavoriteMovies(long chatId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            List<Movie> movies;
            prepareMessage(chatId, "В вашем избранном: ");
            movies = movieDao.findFavoritesByUserId(chatId);
            for (Movie movie : movies) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText(movie.toString());
                sendMessage.setReplyMarkup(telegramKeyboardBuilder.buttonWithFavoriteMovie(movie));
                sendMessage(sendMessage);
            }
        }
    }

    private void showMyLikedMovies(long chatId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            List<Movie> movies;
            prepareMessage(chatId, "Вы лайкнули: ");
            movies = movieDao.findLikedByUserId(chatId);
            for (Movie movie : movies) {
                prepareMessage(chatId, movie.toString());
            }
        }
    }

    private void searchMovie(Message message) {
        Chat chat = message.getChat();
        Long chatId = chat.getId();
        List<Movie> foundMovie = movieDao.findByName(message.getText());
        if (foundMovie.isEmpty()) {
            prepareMessage(chatId, "По вашему запросу ничего не найдено");
        } else {
            for (Movie movie :
                    foundMovie) {
                String imgUrl = movie.getPosterUrl();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText(movie.toString());
                sendMessage.setReplyMarkup(telegramKeyboardBuilder.buttonWithMovie(movie));
                sendMessage(sendMessage);
                sendImage(chatId, imgUrl);
            }
        }
    }
    // TODO: 01.06.2023 определить Chat и chatId вне методов

    //    метод изменения сообщения с кнопками при нажатии на них
    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

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
