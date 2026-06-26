package com.cloudnest.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class FileResponse {

    private UUID id;

    private String originalFilename;

    private String contentType;

    private Long size;

    private String storageKey;
}