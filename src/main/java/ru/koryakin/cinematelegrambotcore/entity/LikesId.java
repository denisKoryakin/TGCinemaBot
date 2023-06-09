package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LikesId implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "movie_id")
    Long movieId;
}
