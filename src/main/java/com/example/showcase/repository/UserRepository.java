package com.example.showcase.repository;

import com.example.showcase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByLogin(String login);

    User findByEmail(String email);
}
