package ru.koryakin.cinematelegrambotcore.service;

public interface MovieEvaluator {

    public String like(long chatId, String movieId);

    String dislike(long chatId, String movieId);

    String toFavorite(long chatId, String movieId);

    String deleteAtMyFavorite(long chatId, String movieId);
}
