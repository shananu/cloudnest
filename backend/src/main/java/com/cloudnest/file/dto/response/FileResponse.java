package com.cloudnest.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FileResponse {

    private UUID id;

    private String originalFilename;

    private String contentType;

    private Long size;
}