package com.cloudnest.storage.service.impl;

import com.cloudnest.config.MinioProperties;
import com.cloudnest.storage.model.StoredFile;
import com.cloudnest.storage.service.StorageService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @Override
    public StoredFile store(MultipartFile file) throws IOException {

        try {

            String storedFilename =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(storedFilename)
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1
                            )
                            .contentType(file.getContentType())
                            .build()
            );

            return new StoredFile(
                    storedFilename,
                    storedFilename
            );

        } catch (Exception e) {

            throw new IOException("Failed to upload file to MinIO", e);

        }

    }

    @Override
    public Resource read(String storedFilename) throws IOException {

        try {

            return new InputStreamResource(

                    minioClient.getObject(

                            GetObjectArgs.builder()
                                    .bucket(properties.getBucket())
                                    .object(storedFilename)
                                    .build()

                    )

            );

        } catch (Exception e) {

            throw new IOException("Failed to read file from MinIO", e);

        }

    }

    @Override
    public void delete(String storedFilename) throws IOException {

        try {

            minioClient.removeObject(

                    RemoveObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(storedFilename)
                            .build()

            );

        } catch (Exception e) {

            throw new IOException("Failed to delete file from MinIO", e);

        }

    }

}