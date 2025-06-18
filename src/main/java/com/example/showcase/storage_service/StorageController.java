package com.example.showcase.storage_service;

import com.example.showcase.service.ProjectService;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://showcase-pd.ru"})
@RequestMapping("/api")
@AllArgsConstructor
public class StorageController {
    private S3Service storageService;
    private ProjectService projectService;

    /**
     * Получение главного скриншота проекта
     *
     * @param projectId
     * @return
     */
    @GetMapping("/projects/{id}/main_screenshot")
    public ResponseEntity<byte[]> getMainScreenshot(@PathVariable("id") int projectId) {
        String mainScreenshotFileName = projectService.getProjectById(projectId).getMainScreenshot();
        if (storageService.fileExists(mainScreenshotFileName) && !mainScreenshotFileName.isEmpty()) {
            try {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(storageService.getFile(mainScreenshotFileName));
            } catch (MinioException e) {
                throw new RuntimeException(e);
            }
        } else
            return ResponseEntity.notFound().build();
    }

    /**
     * Получение количества кол-ва скриншотов проекта
     *
     * @return
     */
    @Deprecated
    @GetMapping("/projects/{projectId}/screenshots/count")
    public ResponseEntity<Integer> getScreenshotsCount(@PathVariable("projectId") int projectId) {
        List<String> screenshotsFileNameList = projectService.getProjectById(projectId).getScreenshots();
        return ResponseEntity.ok()
                .body(screenshotsFileNameList.isEmpty() ? 0 : screenshotsFileNameList.size());
    }


    /**
     * Получение скриншота проекта по его номеру
     *
     * @param projectId
     * @param imageNumber
     * @return
     */
    //TODO test
    @GetMapping("/projects/{projectId}/screenshots/{imageNumber}") //начало с нуля ???
    public ResponseEntity<byte[]> getAllScreenshots(@PathVariable("projectId") int projectId, @PathVariable("imageNumber") int imageNumber) {
        List<String> screenshotsFileNameList = projectService.getProjectById(projectId).getScreenshots();
        if (screenshotsFileNameList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (screenshotsFileNameList.size() < imageNumber) {
            return ResponseEntity.notFound().build();
        }
        if (!storageService.fileExists(screenshotsFileNameList.get(imageNumber))) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(storageService.getFile(screenshotsFileNameList.get(imageNumber)));
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }
    }
}
