package com.cloudnest.trash.dto;

import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.folder.dto.response.FolderResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrashResponse {

    private List<FileResponse> files;

    private List<FolderResponse> folders;

}