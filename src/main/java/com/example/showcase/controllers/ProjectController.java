package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Project;
import com.example.showcase.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://sfedu-project-showcase-seven.vercel.app"})
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO project) {
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
    public ResponseEntity<Project> updateProject(@PathVariable("id") int projectId, @RequestBody ProjectDTO updateProject) {
        Project project = projectService.updateProject(projectId, updateProject);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") int projectId) {
       projectService.deleteProject(projectId);
       return ResponseEntity.ok("Project is deleted");
    }
}
