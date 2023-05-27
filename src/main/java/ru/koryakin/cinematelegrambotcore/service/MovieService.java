package ru.koryakin.cinematelegrambotcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koryakin.cinematelegrambotcore.entity.Movie;
import ru.koryakin.cinematelegrambotcore.repository.MovieDao;
import ru.koryakin.cinematelegrambotcore.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private List<Movie> movies = new ArrayList<>();
    // TODO: 23.05.2023 перенести в метод

    @Autowired
    MovieDao movieDao;
    @Autowired
    JsonParser jsonParser;

    public void readAllToList() {
        jsonParser.jsonToList(jsonParser.getCinemaDb().getPath(), movies);
    }

    public void saveAllToDb() {
        for (Movie movie : movies) {
            movieDao.save(movie);
        }
    }
}
