package com.example.showcase.service;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Project;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDTO projectDTO);

    Project getProjectById(int projectId);

    List<Project> getAllProjects();

    Project updateProject(int projectId, ProjectDTO updateProjectDTO);

    void deleteProject(int projectId);

    Iterable<Project> save(List<Project> projects);

    Iterable<Project> saveProjectsFromDTO(List<ProjectDTO> projectDTOs);

    byte[] downloadMainImageFromFileSystem(Integer projectId) throws IOException;

    @Transactional(readOnly = true)
    List<Project> getProjectsByTags(List<String> tagNames);


    @Transactional(readOnly = true)
    List<Project> getProjectsByTrack(String trackName);

    @Transactional(readOnly = true)
    List<Project> getProjectsByTrackAndTags(String trackName, String dateName, List<String> tagNames);

    //TODO проверка уникальности пользователя (сейчас по названию проекта)
    boolean existsByTitle(String projectTitle);

    List<Project> getProjectsByUserId(int userId);

    List<Project> getProjectsByUserFullName(String fullName);

    Project updateProjectTitle(int projectId, String title);

    Project updateProjectDescription(int projectId, String description);

    Project updateProjectGrade(int projectId, Integer grade);

    Project updateProjectPresentation(int projectId, String presentation);

    Project updateProjectRepo(int projectId, String repo);

    Project updateProjectDate(int projectId, int dateId);

    Project updateProjectTrack(int projectId, int trackId);

    Project addUserToProject(int projectId, int userId);

    Project removeUserFromProject(int projectId, int userId);

    Project addTagToProject(int projectId, int tagId);

    Project removeTagFromProject(int projectId, int tagId);
}
