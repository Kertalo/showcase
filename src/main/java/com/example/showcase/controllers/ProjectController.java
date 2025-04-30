package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Project;
import com.example.showcase.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://sfedu-project-showcase.onrender.com"})
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@ModelAttribute ProjectDTO project) {
        Project savedProject = projectService.createProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") int projectId) {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") int projectId, @ModelAttribute ProjectDTO updateProject) {
        Project project = projectService.updateProject(projectId, updateProject);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") int projectId) {
       projectService.deleteProject(projectId);
       return ResponseEntity.ok("Project is deleted");
    }

    @GetMapping("/main_image/{id}")
    public ResponseEntity<?> downloadMainImageFromFileSystem(@PathVariable("id") int projectId) throws IOException {
        byte[] imageData = projectService.downloadMainImageFromFileSystem(projectId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/by-tag")
    public ResponseEntity<?> getProjectsByTags(@RequestParam(value = "tags", required = false) List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty() || tagNames.stream().allMatch(String::isBlank)) {
            return ResponseEntity.badRequest().body("Требуется указать действительное название тега");
        }
        List<Project> projects = projectService.getProjectsByTags(tagNames);
        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Не было найдено проектов по тегам: " + tagNames);
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-track")
    public ResponseEntity<?> getProjectsByTrackName(@RequestParam(value = "track", required = false) String trackName) {
        if (trackName == null || trackName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Требуется указать действительное название трека");
        }
        List<Project> projects = projectService.getProjectsByTrack(trackName);
        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Не было найдено проектов по трекам: " + trackName);
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getProjectsByFilter(
            @RequestParam(value = "track", required = false) String track,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        if ((track == null || track.trim().isEmpty()) &&
                (date == null || date.trim().isEmpty()) &&
                (tags == null || tags.isEmpty() || tags.stream().allMatch(String::isBlank))) {
            return ResponseEntity.ok(projectService.getAllProjects());
        }
        List<Project> projects = projectService.getProjectsByTrackAndTags(track, date, tags);
        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found for filters: track=" + track + ", date=" + date + ", tags=" + tags);
        }
        return ResponseEntity.ok(projects);
    }

}
