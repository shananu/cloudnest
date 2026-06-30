package com.cloudnest.folder.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.folder.dto.request.CreateFolderRequest;
import com.cloudnest.folder.dto.request.RenameFolderRequest;
import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.dto.response.FolderTreeResponse;
import com.cloudnest.folder.service.FolderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(
    name = "Folders",
    description = "Folder management APIs"
)
@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

        private final FolderService folderService;

        @PostMapping
        public ApiResponse<FolderResponse> create(

                        @Valid @RequestBody CreateFolderRequest request,

                        Authentication authentication

        ) {

                return ApiResponse.<FolderResponse>builder()
                                .success(true)
                                .message("Folder created successfully")
                                .data(folderService.create(request, authentication))
                                .build();
        }

        @GetMapping("/tree")
        public ApiResponse<List<FolderTreeResponse>> getFolderTree(
                        Authentication authentication) {

                return ApiResponse.<List<FolderTreeResponse>>builder()
                                .success(true)
                                .message("Folder tree fetched successfully")
                                .data(folderService.getFolderTree(authentication))
                                .build();
        }

        @PatchMapping("/{folderId}")
        public ApiResponse<FolderResponse> rename(

                        @PathVariable UUID folderId,

                        @Valid @RequestBody RenameFolderRequest request,

                        Authentication authentication

        ) {

                return ApiResponse.<FolderResponse>builder()
                                .success(true)
                                .message("Folder renamed successfully")
                                .data(
                                                folderService.rename(
                                                                folderId,
                                                                request,
                                                                authentication))
                                .build();
        }

        @DeleteMapping("/{folderId}")
        public ApiResponse<Void> delete(

                        @PathVariable UUID folderId,

                        Authentication authentication

        ) throws IOException {

                folderService.delete(
                                folderId,
                                authentication);

                return ApiResponse.<Void>builder()
                                .success(true)
                                .message("Folder deleted successfully")
                                .build();
        }
}