package com.cloudnest.share.mapper;

import com.cloudnest.share.dto.response.ShareResponse;
import com.cloudnest.share.entity.ShareLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShareMapper {

    @Mapping(target = "shareUrl", ignore = true)
    ShareResponse toResponse(ShareLink shareLink);

}