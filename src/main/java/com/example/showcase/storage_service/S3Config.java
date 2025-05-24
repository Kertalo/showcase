package com.example.showcase.storage_service;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * В данном сервисе в качестве хранилища используется Minio
 */
@Configuration
public class S3Config {
    @Value("${s3storage.url}")
    private String minioUrl;

    @Value("${s3storage.access_key}")
    private String accessKey;

    @Value("${s3storage.secret_key}")
    private String secretKey;

    @Value("${s3storage.bucket_name}")
    private String bucketName;

    @Bean
    public MinioClient storageClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public String storageBucketName() {
        return bucketName;
    }
}
