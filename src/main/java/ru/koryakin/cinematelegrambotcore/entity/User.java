package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "movies")
@Getter
@Setter
public class User implements Serializable {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "registered_time")
    public Timestamp registeredTime;

    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id")
    )
    private Set<Movie> moviesInFavorite = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id")
    )
    private Set<Movie> moviesLiked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "dislikes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id")
    )
    private Set<Movie> moviesDisliked = new HashSet<>();

    public void addToFavorite(Movie movie) {
        this.moviesInFavorite.add(movie);
        movie.getUsersAddedToFavorite().add(this);
    }

    public void removeAtFavorite(Movie movie) {
        this.moviesInFavorite.remove(movie);
        movie.getUsersAddedToFavorite().remove(this);
    }

    public void likeMovie(Movie movie) {
        this.moviesLiked.add(movie);
        movie.getUsersLiked().add(this);
    }

    public void removeLikeMovie(Movie movie) {
        this.moviesLiked.remove(movie);
        movie.getUsersLiked().remove(this);
    }

    public void dislikeMovie(Movie movie) {
        this.moviesDisliked.add(movie);
        movie.getUsersDisliked().add(this);
    }

    public void removeDislikeMovie(Movie movie) {
        this.moviesDisliked.remove(movie);
        movie.getUsersDisliked().remove(this);
    }

    public User() {
    }

    @Override
    public String toString() {
        return "Пользователь:" + '\n' +
                "ник пользователя: " + firstName + '\n' +
                "имя пользователя: " + userName + '\n' +
                "зарегистрирован: " + registeredTime + "\n";
    }
}
