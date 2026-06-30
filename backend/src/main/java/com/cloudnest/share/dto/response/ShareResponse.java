package com.cloudnest.share.dto.response;

import com.cloudnest.share.entity.SharePermission;

import java.time.Instant;
import java.util.UUID;

public record ShareResponse(

        UUID id,

        String token,

        String shareUrl,

        SharePermission permission,

        Instant expiresAt

) {
}