package com.cloudnest.share.dto.request;

import com.cloudnest.share.entity.SharePermission;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShareRequest {

    @NotNull
    private SharePermission permission;

    private Integer expiresInDays;

}