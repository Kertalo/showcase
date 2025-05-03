package com.example.showcase.repository;

import com.example.showcase.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    boolean existsByName(String name);

    Track getTrackByName(String name);
}
