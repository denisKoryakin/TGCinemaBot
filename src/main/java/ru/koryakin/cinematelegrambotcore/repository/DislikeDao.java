package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koryakin.cinematelegrambotcore.entity.Dislike;
import ru.koryakin.cinematelegrambotcore.entity.DislikeId;

public interface DislikeDao extends JpaRepository<Dislike, DislikeId> {
}
