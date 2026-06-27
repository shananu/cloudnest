package com.cloudnest.storage.config;

import com.cloudnest.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioInitializer {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @PostConstruct
    public void initialize() throws Exception {

        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(properties.getBucket())
                        .build()
        );

        if (!exists) {

            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(properties.getBucket())
                            .build()
            );

            System.out.println("Created MinIO bucket: " + properties.getBucket());

        } else {

            System.out.println("Using existing MinIO bucket: " + properties.getBucket());

        }

    }

}