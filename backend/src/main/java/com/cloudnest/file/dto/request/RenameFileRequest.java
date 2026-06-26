package com.cloudnest.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameFileRequest {

    @NotBlank
    private String filename;

}