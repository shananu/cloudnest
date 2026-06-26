package com.cloudnest.user.mapper;

import com.cloudnest.auth.dto.request.RegisterRequest;
import com.cloudnest.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(RegisterRequest request);

}