package com.cloudnest.file.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.file.dto.response.DownloadFileResponse;
import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import java.util.UUID;

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

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable UUID id,
            Authentication authentication
    ) throws IOException {
    
        DownloadFileResponse response =
                fileService.download(id, authentication);
    
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                response.originalFilename() +
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                response.contentType()
                        )
                )
                .body(response.resource());
    }
}