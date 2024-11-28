package com.example.showcase.repository;

import com.example.showcase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByFullName(String fullName);

    User findByLogin(String login);
}
