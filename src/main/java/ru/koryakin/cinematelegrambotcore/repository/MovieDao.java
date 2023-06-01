package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koryakin.cinematelegrambotcore.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieDao extends JpaRepository<Movie, Integer> {

    List<Optional<Movie>> findByName(String name);
}
