package com.cloudnest.file.mapper;

import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.entity.FileMetadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileResponse toResponse(FileMetadata file);

}