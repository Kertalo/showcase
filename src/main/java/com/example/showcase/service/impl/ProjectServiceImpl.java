package com.example.showcase.service.impl;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.entity.User;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.Project;
import com.example.showcase.repository.ProjectRepository;
import com.example.showcase.repository.TagRepository;
import com.example.showcase.repository.TrackRepository;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private TagRepository tagRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TrackRepository trackRepository;

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Track track = trackRepository.findById(projectDTO.getTrackId())
            .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        List<Integer> tagIds = projectDTO.getTagsId();
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags not found");
        }

        List<Integer> userIds = projectDTO.getUsersId();
        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("One or more users not found");
        }

        Project project = new Project();
        project.setTrack(track);
        project.setTags(tags);
        project.setUsers(users);
        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(int projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateProject(int projectId, ProjectDTO updateProjectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Track track = trackRepository.findById(updateProjectDTO.getTrackId())
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        List<Integer> tagIds = updateProjectDTO.getTagsId();
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags not found");
        }

        List<Integer> userIds = updateProjectDTO.getUsersId();
        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("One or more users not found");
        }

        project.setDescription(updateProjectDTO.getDescription());
        project.setGrade(updateProjectDTO.getGrade());
        project.setRepo(updateProjectDTO.getRepo());
        project.setTitle(updateProjectDTO.getTitle());
        project.setScreenshots(updateProjectDTO.getScreenshots());
        project.setDate(updateProjectDTO.getDate());
        project.setPresentation(updateProjectDTO.getPresentation());

        project.setTrack(track);
        project.setTags(tags);
        project.setUsers(users);
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        projectRepository.delete(project);
    }

    @Override
    public Iterable<Project> save(List<Project> projects) {
        return projectRepository.saveAll(projects);
    }

    @Override
    public Iterable<Project> saveProjectsFromDTO(List<ProjectDTO> projectDTOs) {
        List<Project> projects = new ArrayList<>();

        for (ProjectDTO projectDTO : projectDTOs) {
            Project project = new Project();

            Track track = trackRepository.findById(projectDTO.getTrackId())
                    .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
            project.setTrack(track);

            List<Tag> tags = tagRepository.findAllById(projectDTO.getTagsId());
            if (tags.size() != projectDTO.getTagsId().size()) {
                throw new ResourceNotFoundException("One or more tags not found");
            }
            project.setTags(tags);

            List<User> users = userRepository.findAllById(projectDTO.getUsersId());
            if (users.size() != projectDTO.getUsersId().size()) {
                throw new ResourceNotFoundException("One or more users not found");
            }
            project.setUsers(users);

            project.setId(projectDTO.getId());
            project.setTitle(projectDTO.getTitle());
            project.setDescription(projectDTO.getDescription());
            project.setGrade(projectDTO.getGrade());
            project.setRepo(projectDTO.getRepo());
            project.setScreenshots(projectDTO.getScreenshots());
            project.setPresentation(projectDTO.getPresentation());
            project.setDate(projectDTO.getDate());

            projects.add(project);
        }

        return projectRepository.saveAll(projects);
    }

}
