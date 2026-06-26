package com.cloudnest.file.service;

import com.cloudnest.file.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponse upload(MultipartFile file) throws IOException;

}