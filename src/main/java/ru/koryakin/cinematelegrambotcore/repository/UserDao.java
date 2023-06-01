package ru.koryakin.cinematelegrambotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koryakin.cinematelegrambotcore.entity.User;


public interface UserDao extends JpaRepository<User, Integer> {
}
