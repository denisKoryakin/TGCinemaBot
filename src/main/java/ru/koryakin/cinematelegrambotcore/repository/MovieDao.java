package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.koryakin.cinematelegrambotcore.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieDao extends JpaRepository<Movie, Integer> {

    @Query(nativeQuery = true, value = "select * from movie where UPPER(name) like '%'||UPPER(:name)||'%'")
    List<Optional<Movie>> findByName(String name);
}
