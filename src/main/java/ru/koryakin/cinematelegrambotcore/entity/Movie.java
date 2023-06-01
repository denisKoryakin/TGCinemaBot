package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

    public Movie() {
    }

    @Override
    public String toString() {
        return  name + "\n\n" +
                ", год выпуска: " + year +
                ", страна: " + country + "\n" +
                ", жанр" + genre + "\n" +
                ", IMDB: " + imdbScore;
    }

//    public byte[] showMovie() {
//        byte[] movie;
//        this.toString()
//        return movie;
//    }
    // TODO: 01.06.2023 подумать как передавать постер в ТГ
}
