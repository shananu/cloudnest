package com.cloudnest.share.service;

import com.cloudnest.share.dto.request.CreateShareRequest;
import com.cloudnest.share.dto.response.ShareResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface ShareService {

    ShareResponse createShare(
            UUID fileId,
            CreateShareRequest request,
            Authentication authentication
    );

    List<ShareResponse> getShares(
            UUID fileId,
            Authentication authentication
    );

    void deleteShare(
            UUID shareId,
            Authentication authentication
    );

}