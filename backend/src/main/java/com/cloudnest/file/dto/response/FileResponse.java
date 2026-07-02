package com.cloudnest.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String originalFilename;

    private String contentType;

    private Long size;

    private String storageKey;
}