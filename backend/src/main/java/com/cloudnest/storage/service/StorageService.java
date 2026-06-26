package com.cloudnest.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String store(MultipartFile file) throws IOException;

    byte[] read(String storedFilename) throws IOException;

    void delete(String storedFilename) throws IOException;

}