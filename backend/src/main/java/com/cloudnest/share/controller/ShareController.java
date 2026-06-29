package com.cloudnest.share.controller;

import com.cloudnest.common.response.ApiResponse;
import com.cloudnest.share.dto.request.CreateShareRequest;
import com.cloudnest.share.dto.response.ShareResponse;
import com.cloudnest.share.service.ShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @PostMapping("/files/{fileId}/share")
    public ApiResponse<ShareResponse> createShare(

            @PathVariable UUID fileId,

            @Valid
            @RequestBody CreateShareRequest request,

            Authentication authentication
    ) {

        return ApiResponse.<ShareResponse>builder()
                .success(true)
                .message("Share link created successfully")
                .data(
                        shareService.createShare(
                                fileId,
                                request,
                                authentication
                        )
                )
                .build();
    }

    @GetMapping("/files/{fileId}/shares")
    public ApiResponse<List<ShareResponse>> getShares(

            @PathVariable UUID fileId,

            Authentication authentication
    ) {

        return ApiResponse.<List<ShareResponse>>builder()
                .success(true)
                .message("Shares fetched successfully")
                .data(
                        shareService.getShares(
                                fileId,
                                authentication
                        )
                )
                .build();
    }

    @DeleteMapping("/shares/{shareId}")
    public ApiResponse<Void> deleteShare(

            @PathVariable UUID shareId,

            Authentication authentication
    ) {

        shareService.deleteShare(
                shareId,
                authentication
        );

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Share link deleted successfully")
                .build();
    }

}