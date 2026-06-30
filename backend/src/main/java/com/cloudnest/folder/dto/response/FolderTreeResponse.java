package com.cloudnest.folder.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderTreeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private UUID parentId;

    @Builder.Default
    private List<FolderTreeResponse> children = new ArrayList<>();

}