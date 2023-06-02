package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
public class Movie implements Serializable {

    @Id
    @SequenceGenerator(name = "movieIdSeq", sequenceName = "movie_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movieIdSeq")
    private int id;
    @Column
    private String name;
    @Column(name = "original_name")
    private String originalName;
    @Column
    private Integer year;
    @Column
    private String country;
    @Column
    private String genre;
    @Column(name = "imdb_score")
    private Double imdbScore;
    @Column(name = "poster_url")
    private String posterUrl;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "moviesInFavorite")
    private Set<User> usersAddedToFavorite;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "moviesLiked")
    private Set<User> usersLiked;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "moviesDisliked")
    private Set<User> usersDisliked;

    public Movie() {
    }

    @Override
    public String toString() {
        return name + "\n\n" +
                ", год выпуска: " + year +
                ", страна: " + country + "\n" +
                ", жанр" + genre + "\n" +
                ", IMDB: " + imdbScore;
    }
}
