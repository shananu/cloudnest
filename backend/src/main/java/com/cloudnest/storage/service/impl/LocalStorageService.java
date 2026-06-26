package com.cloudnest.storage.service.impl;

import com.cloudnest.storage.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private static final Path ROOT =
            Paths.get("storage/uploads");

    public LocalStorageService() throws IOException {
        Files.createDirectories(ROOT);
    }

    @Override
    public String store(MultipartFile file) throws IOException {

        String storedFilename =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                ROOT.resolve(storedFilename),
                StandardCopyOption.REPLACE_EXISTING
        );

        return storedFilename;
    }

    @Override
    public byte[] read(String storedFilename) throws IOException {

        return Files.readAllBytes(
                ROOT.resolve(storedFilename)
        );
    }

    @Override
    public void delete(String storedFilename) throws IOException {

        Files.deleteIfExists(
                ROOT.resolve(storedFilename)
        );
    }
}