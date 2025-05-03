package com.example.showcase.repository;

import com.example.showcase.entity.Date;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<Date, Integer> {
    boolean existsByName(String dateName);

    Date getDateByName(String name);
}
