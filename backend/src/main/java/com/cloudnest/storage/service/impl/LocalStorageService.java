package com.cloudnest.storage.service.impl;

import com.cloudnest.storage.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudnest.storage.model.StoredFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private static final Path ROOT = Paths.get("storage/uploads");

    public LocalStorageService() throws IOException {
        Files.createDirectories(ROOT);
    }

    @Override
    public StoredFile store(MultipartFile file) throws IOException {

        String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                ROOT.resolve(storedFilename),
                StandardCopyOption.REPLACE_EXISTING);

        return new StoredFile(
                storedFilename,
                "uploads/" + storedFilename);
    }

    @Override
    public Resource read(String storedFilename) throws IOException {
        Path path = ROOT.resolve(storedFilename);
        return new UrlResource(path.toUri());
    }

    @Override
    public void delete(String storedFilename) throws IOException {

        Files.deleteIfExists(
                ROOT.resolve(storedFilename));
    }
}