package com.cloudnest.trash.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.trash.dto.TrashResponse;
import com.cloudnest.trash.service.TrashService;
import lombok.RequiredArgsConstructor;
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
}