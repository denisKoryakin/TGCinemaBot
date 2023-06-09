package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koryakin.cinematelegrambotcore.entity.Likes;
import ru.koryakin.cinematelegrambotcore.entity.LikesId;

import java.util.List;

public interface LikeDao extends JpaRepository<Likes, LikesId> {

    List<Likes> findById_UserId(long userId);
}
