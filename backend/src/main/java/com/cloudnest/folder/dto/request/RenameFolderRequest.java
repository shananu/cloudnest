package com.cloudnest.folder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameFolderRequest {

    @NotBlank(message = "Folder name is required")
    private String name;

}