package ru.koryakin.cinematelegrambotcore.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.koryakin.cinematelegrambotcore.entity.Movie;
import ru.koryakin.cinematelegrambotcore.repository.MovieDao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonParser {

    private File cinemaDb = new File("C:\\ShareDownloads\\Films\\films.json");

    @Autowired
    MovieDao movieDao;

    public void updateDb() {
        List<Movie> movies = new ArrayList<>();
        jsonToList(cinemaDb.getPath(), movies);
        for (Movie movie : movies) {
            movieDao.save(movie);
        }
    }

    public static String listToJson(List<Movie> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static List<Movie> jsonToList(String json, List<Movie> movies) {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        try {
            Object obj = parser.parse(new FileReader(json));
            JsonArray array = (JsonArray) obj;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            for (int i = 0; i < array.size(); i++) {
                Movie movie = gson.fromJson(String.valueOf(array.get(i)), Movie.class);
                movies.add(movie);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return movies;
    }
}
