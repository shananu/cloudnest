package com.cloudnest.folder.mapper;

import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.entity.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(target = "parentId", source = "parent.id")
    FolderResponse toResponse(Folder folder);

}

