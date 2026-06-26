package com.cloudnest.file.dto.response;

import org.springframework.core.io.Resource;

public record DownloadFileResponse(
        Resource resource,
        String originalFilename,
        String contentType
) {
}