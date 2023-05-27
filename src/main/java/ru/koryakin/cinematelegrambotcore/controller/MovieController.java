package ru.koryakin.cinematelegrambotcore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryakin.cinematelegrambotcore.service.MovieService;

@RestController
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/init")
    public void saveAllToDb() {
        movieService.readAllToList();
        movieService.saveAllToDb();
    }
}
