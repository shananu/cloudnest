package com.cloudnest.trash.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.trash.dto.TrashResponse;
import com.cloudnest.trash.service.TrashService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trash")
@RequiredArgsConstructor
public class TrashController {

    private final TrashService trashService;

    @GetMapping
    public ApiResponse<TrashResponse> getTrash(
            Authentication authentication) {

        return ApiResponse.<TrashResponse>builder()
                .success(true)
                .message("Trash fetched successfully")
                .data(trashService.getTrash(authentication))
                .build();
    }

    @PostMapping("/files/{id}/restore")
    public ApiResponse<Void> restoreFile(

            @PathVariable UUID id,

            Authentication authentication

    ) {

        trashService.restoreFile(id, authentication);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("File restored successfully")
                .build();
    }

    @PostMapping("/folders/{id}/restore")
    public ApiResponse<Void> restoreFolder(

            @PathVariable UUID id,

            Authentication authentication

    ) {

        trashService.restoreFolder(id, authentication);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Folder restored successfully")
                .build();
    }

    @DeleteMapping("/files/{id}")
    public ApiResponse<Void> permanentlyDeleteFile(
            @PathVariable UUID id,
            Authentication authentication) throws IOException {

        trashService.permanentlyDeleteFile(id, authentication);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("File permanently deleted")
                .build();
    }

    @DeleteMapping("/folders/{id}")
    public ApiResponse<Void> permanentlyDeleteFolder(
            @PathVariable UUID id,
            Authentication authentication) throws IOException {

        trashService.permanentlyDeleteFolder(id, authentication);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Folder permanently deleted")
                .build();
    }
}