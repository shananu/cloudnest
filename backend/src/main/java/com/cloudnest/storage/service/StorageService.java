package com.cloudnest.storage.service;

import org.springframework.web.multipart.MultipartFile;
import com.cloudnest.storage.model.StoredFile;

import java.io.IOException;

public interface StorageService {

    StoredFile store(MultipartFile file) throws IOException;

    org.springframework.core.io.Resource read(String storedFilename) throws IOException;

    void delete(String storedFilename) throws IOException;

}