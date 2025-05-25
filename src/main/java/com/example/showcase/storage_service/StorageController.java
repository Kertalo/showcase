package com.example.showcase.storage_service;

import com.example.showcase.entity.Project;
import com.example.showcase.service.ProjectService;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://sfedu-project-showcase.onrender.com"})
@RequestMapping("/api")
@AllArgsConstructor
public class StorageController {
    private S3Service storageService;
    private ProjectService projectService;

    //TODO test
    @GetMapping("/projects/{id}/main_screenshot")
    public ResponseEntity<byte[]> getMainScreenshot(@PathVariable("id") int projectId) {
        String mainScreenshotFileName = projectService.getProjectById(projectId).getMainScreenshot();
        if (storageService.fileExists(mainScreenshotFileName)){
            try {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(storageService.getFile(mainScreenshotFileName));
            } catch (MinioException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return ResponseEntity.notFound().build();
    }
}
