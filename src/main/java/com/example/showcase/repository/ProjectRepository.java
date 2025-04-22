package com.example.showcase.repository;

import com.example.showcase.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT DISTINCT p FROM Project p JOIN p.tags t LEFT JOIN FETCH p.tags WHERE t.name IN :tagNames")
    List<Project> findByTagNames(@Param("tagNames") List<String> tagNames);


    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tags WHERE p.track.name = :trackName")
    List<Project> findByTrackName(@Param("trackName") String trackName);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.tags " +
            "WHERE (:trackName IS NULL OR p.track.name = :trackName) " +
            "AND (:tagNames IS NULL OR EXISTS (SELECT t FROM p.tags t WHERE t.name IN :tagNames))")
    List<Project> findByTrackAndTags(@Param("trackName") String trackName, @Param("tagNames") List<String> tagNames);
}
