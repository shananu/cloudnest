package com.cloudnest.folder.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FolderResponse {

    private UUID id;

    private String name;

    private UUID parentId;

}