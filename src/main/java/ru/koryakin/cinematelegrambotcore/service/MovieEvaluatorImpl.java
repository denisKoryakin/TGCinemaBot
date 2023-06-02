package ru.koryakin.cinematelegrambotcore.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.koryakin.cinematelegrambotcore.entity.User;
import ru.koryakin.cinematelegrambotcore.repository.MovieDao;
import ru.koryakin.cinematelegrambotcore.repository.UserDao;

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

    // TODO: 02.06.2023 возможно стоит добавить логику исключения лайком дислайка и наоборот

    @Override
    public String like(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            user.get().likeMovie(movieDao.findById(Integer.parseInt(movieId)).get());
            log.info("Поставлен лайк от пользователя {} фильму {}", chatId, movieId);
            return "Вы поставили лайк";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String dislike(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            user.get().dislikeMovie(movieDao.findById(Integer.parseInt(movieId)).get());
            log.info("Поставлен дизлайк от пользователя {} фильму {}", chatId, movieId);
            return "Вы поставили дизлайк";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String toFavorite(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            user.get().addToFavorite(movieDao.findById(Integer.parseInt(movieId)).get());
            log.info("Фильм {} добавлен в избранное пользователем {} ", movieId, chatId);
            return "Фильм добавлен в избранное";
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }

    @Override
    public String deleteAtMyFavorite(long chatId, String movieId) {
        Optional<User> user = userDao.findById((int) chatId);
        if (user.isPresent()) {
            if (user.get().getMoviesInFavorite().contains(movieDao.findById(Integer.parseInt(movieId)).get())) {
                user.get().removeAtFavorite(movieDao.findById(Integer.parseInt(movieId)).get());
                log.info("Фильм {} удален из избранного пользователя {} ", movieId, chatId);
                return "Фильм удален из избранного";
            } else {
                return "Фильм не находится в вашем изьранном";
            }
        } else {
            return NOT_REGISTRY_ANSWER;
        }
    }
}
