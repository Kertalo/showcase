package com.example.showcase.repository;

import com.example.showcase.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);

    Tag getTagByName(String name);
}
