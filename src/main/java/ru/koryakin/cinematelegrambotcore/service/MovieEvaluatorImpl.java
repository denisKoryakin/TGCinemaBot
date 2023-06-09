package ru.koryakin.cinematelegrambotcore.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.koryakin.cinematelegrambotcore.entity.*;
import ru.koryakin.cinematelegrambotcore.repository.*;

import java.util.Optional;

@Transactional
@Slf4j
@Component
public class MovieEvaluatorImpl implements MovieEvaluator {

    private final String NOT_REGISTRY_ANSWER = "Для этого необходимо зарегистрироваться по команде /start";

    @Autowired
    UserDao userDao;
    @Autowired
    MovieDao movieDao;
    @Autowired
    FavoriteDao favoriteDao;
    @Autowired
    LikeDao likeDao;
    @Autowired
    DislikeDao dislikeDao;

    // TODO: 02.06.2023 возможно стоит добавить логику исключения лайком дислайка и наоборот

    @Override
    public String like(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            likeDao.save(new Likes(new LikesId(chatId, Long.parseLong(movieId))));
            log.info("Поставлен лайк от пользователя {} фильму {}", chatId, movieId);
            return "Фильму " + "'" + movieDao.findById(Integer.parseInt(movieId)).get().getName() + "'" + " поставлен лайк";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String dislike(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            dislikeDao.save(new Dislike(new DislikeId(chatId, Long.parseLong(movieId))));
            log.info("Поставлен дизлайк от пользователя {} фильму {}", chatId, movieId);
            return "Фильму " + "'" + movieDao.findById(Integer.parseInt(movieId)).get().getName() + "'" + " поставлен дизлайк";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String toFavorite(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            favoriteDao.save(new Favorite(new FavoriteId(chatId, Long.parseLong(movieId))));
            log.info("Фильм {} добавлен в избранное пользователем {}",movieId, chatId);
            return "Фильм " + "'" + movieDao.findById(Integer.parseInt(movieId)).get().getName() + "'" + " добавлен в избранное";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String deleteAtMyFavorite(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
                favoriteDao.delete(new Favorite(new FavoriteId(chatId, Long.parseLong(movieId))));
                log.info("Фильм {} удален из избранного пользователя {} ", movieId, chatId);
                return "Фильм " + "'" + movieDao.findById(Integer.parseInt(movieId)).get().getName() + "'" + " удален из избранного";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }
}
