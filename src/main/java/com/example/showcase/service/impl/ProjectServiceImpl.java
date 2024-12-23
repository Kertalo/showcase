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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private TagRepository tagRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TrackRepository trackRepository;

    private static final String FOLDER_PATH = "C:\\JAVA\\PROJECTS\\showcase\\project_images\\main_screenshots\\";
    private static final String FOLDER_PATH_SCREENSHOTS = "C:\\JAVA\\PROJECTS\\showcase\\project_images\\screenshots\\";

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

        String imagePath = saveImage(projectDTO.getMainScreenshot(), projectDTO.getTitle());
        List<String> screenshotsPaths = saveImages(projectDTO.getScreenshots(), projectDTO.getTitle());


        Project project = new Project();

        project.setTrack(track);
        project.setTags(tags);
        project.setUsers(users);
        project.setDescription(projectDTO.getDescription());
        project.setTitle(projectDTO.getTitle());
        project.setRepo(projectDTO.getRepo());
        project.setGrade(projectDTO.getGrade());
        project.setDate(projectDTO.getDate());

        project.setMainScreenshot(imagePath);
        project.setScreenshots(screenshotsPaths);

        project.setPresentation(projectDTO.getPresentation());

        return projectRepository.save(project);
    }

    private String saveImage(MultipartFile image, String title) {
        File directory = new File(FOLDER_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = FOLDER_PATH + title + ".png";
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }

        return filePath;
    }

    private List<String> saveImages(MultipartFile[] images, String title) {
        List<String> imagePaths = new ArrayList<>();
        File directory = new File(FOLDER_PATH_SCREENSHOTS + title);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            String fileName = title + "_screenshot_" + i+1 + ".png";
            String filePath = directory.getAbsolutePath() + File.separator + fileName;

            try {
                image.transferTo(new File(filePath));
                imagePaths.add(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage());
            }
        }

        return imagePaths;
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
        project.setDate(updateProjectDTO.getDate());
        project.setPresentation(updateProjectDTO.getPresentation());

        project.setTrack(track);
        project.setTags(tags);
        project.setUsers(users);

        if (updateProjectDTO.getMainScreenshot() != null && !updateProjectDTO.getMainScreenshot().isEmpty()) {
            String mainImagePath = saveImage(updateProjectDTO.getMainScreenshot(), project.getTitle());
            project.setMainScreenshot(mainImagePath);
        }

        if (updateProjectDTO.getScreenshots() != null && updateProjectDTO.getScreenshots().length > 0) {
            List<String> screenshotsPaths = saveImages(updateProjectDTO.getScreenshots(), project.getTitle());
            project.setScreenshots(screenshotsPaths);
        }

        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (project.getMainScreenshot() != null && !project.getMainScreenshot().isEmpty()) {
            File file = new File(project.getMainScreenshot());
            if (file.exists() && file.isFile()) {
                if (!file.delete()) {
                    throw new RuntimeException("Failed to delete file");
                }
            }
        }
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
            project.setPresentation(projectDTO.getPresentation());
            project.setDate(projectDTO.getDate());

            projects.add(project);
        }

        return projectRepository.saveAll(projects);
    }

    @Override
    public byte[] downloadMainImageFromFileSystem(Integer projectId) throws IOException {
        Optional<Project> project = projectRepository.findById(projectId);
        String filePath = project.get().getMainScreenshot();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

}
