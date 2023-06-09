package ru.koryakin.cinematelegrambotcore.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.koryakin.cinematelegrambotcore.entity.Movie;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramKeyboardBuilder {

//    стартовая клавиатура
    public ReplyKeyboardMarkup startKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("По жанрам");
        row1.add("Случайная подборка");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Топ лайков");
        row2.add("Топ избранного");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Мои лайки");
        row3.add("Мое избранное");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

//    клавиатура с удалением метаданных пользователя
    public InlineKeyboardMarkup deleteButtons() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var deleteButton = new InlineKeyboardButton();
        deleteButton.setText("Да");
        deleteButton.setCallbackData("DELETE_DATA_BUTTON");

        var notDeleteButton = new InlineKeyboardButton();
        notDeleteButton.setText("Нет");
        notDeleteButton.setCallbackData("NOT_DELETE_DATA_BUTTON");

        rowInLine.add(deleteButton);
        rowInLine.add(notDeleteButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

//    клавиатура под сообщением с фильмом
    public InlineKeyboardMarkup buttonWithMovie(Movie movie) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        String movieId = String.valueOf(movie.getId());

        var likeButton = new InlineKeyboardButton();
        likeButton.setText("like");
        likeButton.setCallbackData("LIKE_BUTTON_" + movieId);

        var dislikeButton = new InlineKeyboardButton();
        dislikeButton.setText("dislike");
        dislikeButton.setCallbackData("DISLIKE_BUTTON_" + movieId);

        var favoritesButton = new InlineKeyboardButton();
        favoritesButton.setText("в Избранное");
        favoritesButton.setCallbackData("ADD_TO_MY_FAVORITES_BUTTON_" + movieId);

        rowInLine.add(likeButton);
        rowInLine.add(dislikeButton);
        rowInLine.add(favoritesButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

//    клавиатура под сообщением с фильмом из избранного
    public InlineKeyboardMarkup buttonWithFavoriteMovie(Movie movie) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        String movieId = String.valueOf(movie.getId());

        var deleteAtMyFavoritesButton = new InlineKeyboardButton();
        deleteAtMyFavoritesButton.setText("убрать из Избранного");
        deleteAtMyFavoritesButton.setCallbackData("DELETE_AT_MY_FAVORITES_BUTTON_" + movieId);

        rowInLine.add(deleteAtMyFavoritesButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }
}
