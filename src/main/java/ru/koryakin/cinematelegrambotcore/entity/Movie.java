package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "movie")
@Getter
@Setter
/** @Entity - класс необходимо хранить в БД, название таблицы - "movie" **/
public class Movie implements Serializable {

    @Id
    @SequenceGenerator(name = "movieIdSeq", sequenceName = "movie_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "movieIdSeq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /* @Column - мапит поля класса на колонки таблицы */
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

    public Movie() {
    }

    public Movie(String name, String originalName, Integer year, String country, String genre, Double imdbScore, String posterUrl) {
        this.name = name;
        this.originalName = originalName;
        this.year = year;
        this.country = country;
        this.genre = genre;
        this.imdbScore = imdbScore;
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "entities.Movie{" +
                "name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", year=" + year +
                ", country='" + country + '\'' +
                ", genres=" + genre +
                ", imdbScore=" + imdbScore +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
