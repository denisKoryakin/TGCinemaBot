package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.koryakin.cinematelegrambotcore.entity.Movie;

import java.util.List;

public interface MovieDao extends JpaRepository<Movie, Integer> {

    @Query(nativeQuery = true, value = "select * from movie where UPPER(name) like '%'||UPPER(:name)||'%'")
    List<Movie> findByName(String name);

    @Query(nativeQuery = true, value = "select movie.* from movie join favorite on movie.id = favorite.movie_id where user_id = :userId")
    List<Movie> findFavoritesByUserId(long userId);

    @Query(nativeQuery = true, value = "select movie.* from movie join likes on movie.id = likes.movie_id where user_id = :userId")
    List<Movie> findLikedByUserId(long userId);
}
