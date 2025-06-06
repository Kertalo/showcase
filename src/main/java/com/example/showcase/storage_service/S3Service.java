package com.example.showcase.storage_service;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class S3Service {

    @Autowired
    protected final MinioClient storageClient;

    @Autowired
    protected final String bucketName;

    /**
     * Загрузка файла с добавлением сгенерированного имени
     */
    public void uploadFile(String fileName,MultipartFile file) throws IOException, MinioException {
        try {
            storageClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1) // подобрать параметры
                            .contentType(file.getContentType())
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new MinioException("Error uploading file to S3 Storage: " + e.getMessage());
        }
    }

    /**
     * Получение файла с добавлением сгенерированного имени
     * (С закрытием InputStream)
     */
    public byte[] getFile(String fileName) throws MinioException {
        try (InputStream inputStream = storageClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(fileName).build())) {
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new MinioException("File download failed: " + e.getMessage());
        }
    }

    /**
     * Удаление файла по имени
     */
    public void deleteFile(String fileName) throws MinioException {
        try {
            storageClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            throw new MinioException("File deletion failed: " + e.getMessage());
        }
    }

    /**
     * Перезапись содержимого файла по имени
     */
    public void updateFile(String existingFileName, MultipartFile newFile) throws IOException, MinioException {
        deleteFile(existingFileName);
        uploadFile(existingFileName,newFile);
    }

    /**
     * Проверка существования файла
     */
    public boolean fileExists(String fileName) {
        try {
            storageClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            System.out.println("Object exists");
            return true;
        } catch (MinioException e) {
            System.out.println("Object doesn't exists");
            return false;
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException e){
            System.out.println("Error checking file existence" + e.getMessage());
            return false;
        }
    }
}
