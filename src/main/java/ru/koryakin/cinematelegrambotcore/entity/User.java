package ru.koryakin.cinematelegrambotcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

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

    public User() {
    }

    @Override
    public String toString() {
        return "Пользователь:" + '\n' +
                "ник пользователя: " + firstName + '\n' +
                "имя пользователя: " + userName + '\n' +
                "зарегистрирован: " + registeredTime + "\n"
//                +
//                "фильмов в избранном: " + moviesInFavorite.size() +
//                "поставлено лайков: " + moviesLiked.size() +
//                "поставлено дислайков: " + moviesDisliked
                ;
    }
}
