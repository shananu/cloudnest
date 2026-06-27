package com.cloudnest.folder.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class FolderTreeResponse {

    private UUID id;

    private String name;

    private UUID parentId;

    @Builder.Default
    private List<FolderTreeResponse> children = new ArrayList<>();

}