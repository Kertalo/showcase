package com.example.showcase.service;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDTO projectDTO);

    Project getProjectById(int projectId);

    List<Project> getAllProjects();

    Project updateProject(int projectId, ProjectDTO updateProjectDTO);

    void deleteProject(int projectId);

    Iterable<Project> save(List<Project> projects);
}
