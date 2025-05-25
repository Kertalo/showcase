package com.example.showcase.service.impl;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.*;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.repository.*;
import com.example.showcase.service.DateService;
import com.example.showcase.service.ProjectService;
import com.example.showcase.service.TrackService;
import com.example.showcase.service.UserService;
import com.example.showcase.storage_service.FileNameGenerator;
import com.example.showcase.storage_service.Prefix;
import com.example.showcase.storage_service.S3Service;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final DateRepository dateRepository;
    private TagRepository tagRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TrackRepository trackRepository;
    private DateService dateService;
    private TrackService trackService;
    private UserService userService;

    //Сервис для работы с S3-хранилищем
    private S3Service storageService;

    @Override
    public Project createProject(ProjectDTO projectDTO) {

        Date date = dateRepository.findById(projectDTO.getDateId())
                .orElseThrow(() -> new ResourceNotFoundException("Date not found"));

        Track track = trackRepository.findById(projectDTO.getTrackId())
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        List<Integer> tagIds = projectDTO.getTagsId();
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags not found");
        }
        List<User> users = new ArrayList<>();
        if (projectDTO.getUsersId() != null && !projectDTO.getUsersId().isEmpty()) {
            users = userRepository.findAllById(projectDTO.getUsersId());
        }
//        if (users.size() != userIds.size()) {
//            throw new ResourceNotFoundException("One or more users not found");
//        }

        String imagePath = saveImage(projectDTO.getMainScreenshot(), projectDTO.getTitle());
        List<String> screenshotsPaths = saveImages(projectDTO.getScreenshots(), projectDTO.getTitle());


        Project project = new Project();

        project.setTrack(track);
        project.setTags(tags);
        project.setUsers(users);
        project.setDate(date);
        project.setDescription(projectDTO.getDescription());
        project.setTitle(projectDTO.getTitle());
        project.setRepo(projectDTO.getRepo());
        project.setGrade(projectDTO.getGrade());

        project.setMainScreenshot(imagePath);
        project.setScreenshots(screenshotsPaths);

        project.setPresentation(projectDTO.getPresentation());

        return projectRepository.save(project);
    }

    private String saveImage(MultipartFile image, String title) {

        String fileName = FileNameGenerator.generateFileName(Prefix.PROJECT_MAIN_IMAGE, title);

        try {
            storageService.uploadFile(fileName, image);
        } catch (IOException | MinioException e) {
            throw new RuntimeException("Error uploading image " + e.getMessage());
        }

        return fileName;
    }

    private List<String> saveImages(MultipartFile[] images, String title) {
        List<String> fileNameList = new ArrayList<>();

        for (MultipartFile image : images) {
            //String fileName = title + "_screenshot_"+ i + ".png";
            String fileName = FileNameGenerator.generateFileName(Prefix.PROJECT_OTHER_SCREENSHOTS, title);
            fileNameList.add(fileName);
            try {
                storageService.uploadFile(fileName, image);
            } catch (IOException | MinioException e) {
                throw new RuntimeException("Error uploading image " + e.getMessage());
            }
        }

        return fileNameList;
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

        Date date = dateRepository.findById(updateProjectDTO.getDateId())
                .orElseThrow(() -> new ResourceNotFoundException("Date not found"));

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
        project.setPresentation(updateProjectDTO.getPresentation());

        project.setTrack(track);
        project.setDate(date);
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

        //удаление файла из projects.image
        if (project.getMainScreenshot() != null && !project.getMainScreenshot().isEmpty()) {
            String fileName = project.getMainScreenshot();
            if (storageService.fileExists(fileName)) {
                try {
                    storageService.deleteFile(fileName);
                } catch (MinioException e) {
                    throw new RuntimeException("Failed to delete main screenshot" + e.getMessage());
                }
            }
        }

        //удаление списка файлов из projects.screenshots
        if (!project.getScreenshots().isEmpty()) {
            for (String fileName : project.getScreenshots()) {
                if (storageService.fileExists(fileName)) {
                    try {
                        storageService.deleteFile(fileName);
                    } catch (MinioException e) {
                        throw new RuntimeException("Failed to delete main screenshot" + e.getMessage());
                    }
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

            Date date = dateRepository.findById(projectDTO.getDateId())
                    .orElseThrow(() -> new ResourceNotFoundException("Date not found"));
            project.setDate(date);

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

            projects.add(project);
        }

        return projectRepository.saveAll(projects);
    }

    @Override
    public byte[] downloadMainImageFromFileSystem(Integer projectId) throws IOException {
        Optional<Project> project = projectRepository.findById(projectId);
        String fileName = project.get().getMainScreenshot();
        byte[] images = null;
        try {
            images = storageService.getFile(fileName);
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> getProjectsByTags(List<String> tagNames) {
        return projectRepository.findByTagNames(tagNames);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> getProjectsByTrack(String trackName) {
        return projectRepository.findByTrackName(trackName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> getProjectsByTrackAndTags(String trackName, String dateName, List<String> tagNames) {
        if ((trackName == null || trackName.trim().isEmpty()) &&
                (dateName == null || dateName.trim().isEmpty()) &&
                (tagNames == null || tagNames.isEmpty() || tagNames.stream().allMatch(String::isBlank))) {
            return List.of();
        }
        if (trackName != null && trackName.trim().isEmpty()) {
            trackName = null;
        }
        if (dateName != null && dateName.trim().isEmpty()) {
            dateName = null;
        }
        if (tagNames != null && (tagNames.isEmpty() || tagNames.stream().allMatch(String::isBlank))) {
            tagNames = null;
        }
        return projectRepository.findByTrackAndTags(trackName, dateName, tagNames);
    }

    //TODO проверка уникальности пользователя (сейчас по ФИО и курсу)
    @Override
    public boolean existsByTitle(String projectTitle) {
        return projectRepository.existsProjectByTitle(projectTitle);
    }


    @Override
    public List<Project> getProjectsByUserId(int userId) {
        return projectRepository.findByUsersId(userId);
    }

    @Override
    public List<Project> getProjectsByUserFullName(String fullName) {
        return projectRepository.findByUserFullName(fullName);
    }

    @Override
    public Project updateProjectTitle(int projectId, String title) {
        Project project = getProjectById(projectId);
        project.setTitle(title);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectDescription(int projectId, String description) {
        Project project = getProjectById(projectId);
        project.setDescription(description);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectGrade(int projectId, Integer grade) {
        Project project = getProjectById(projectId);
        project.setGrade(grade);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectPresentation(int projectId, String presentation) {
        Project project = getProjectById(projectId);
        project.setPresentation(presentation);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectRepo(int projectId, String repo) {
        Project project = getProjectById(projectId);
        project.setRepo(repo);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectDate(int projectId, int dateId) {
        Project project = getProjectById(projectId);
        Date date = dateService.getDateById(dateId);
        project.setDate(date);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectTrack(int projectId, int trackId) {
        Project project = getProjectById(projectId);
        Track track = trackService.getTrackById(trackId);
        project.setTrack(track);
        return projectRepository.save(project);
    }

    @Override
    public Project addUserToProject(int projectId, int userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);
        project.getUsers().add(user);
        return projectRepository.save(project);
    }

    @Override
    public Project removeUserFromProject(int projectId, int userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);
        project.getUsers().remove(user);
        return projectRepository.save(project);
    }

}
