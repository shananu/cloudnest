package com.cloudnest.trash.dto;

import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.folder.dto.response.FolderResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrashResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<FileResponse> files;

    private List<FolderResponse> folders;

}