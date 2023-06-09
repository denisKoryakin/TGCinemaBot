package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koryakin.cinematelegrambotcore.entity.Favorite;
import ru.koryakin.cinematelegrambotcore.entity.FavoriteId;

import java.util.List;

public interface FavoriteDao extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findById_UserId(long userId);
}
