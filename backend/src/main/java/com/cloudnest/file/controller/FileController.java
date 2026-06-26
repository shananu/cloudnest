package com.cloudnest.file.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<FileResponse> upload(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        return ApiResponse.<FileResponse>builder()
                .success(true)
                .message("File uploaded successfully")
                .data(fileService.upload(file, authentication))
                .build();
    }

    @GetMapping
    public ApiResponse<List<FileResponse>> getMyFiles(
            Authentication authentication) {

        return ApiResponse.<List<FileResponse>>builder()
                .success(true)
                .message("Files fetched successfully")
                .data(fileService.getMyFiles(authentication))
                .build();
    }
}