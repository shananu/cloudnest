package com.cloudnest.share.service.impl;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.share.dto.request.CreateShareRequest;
import com.cloudnest.share.dto.response.ShareResponse;
import com.cloudnest.share.entity.ShareLink;
import com.cloudnest.share.mapper.ShareMapper;
import com.cloudnest.share.repository.ShareRepository;
import com.cloudnest.share.service.ShareService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.repository.UserRepository;
import com.cloudnest.user.service.CurrentUserService;
import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.config.AppProperties;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    private final ShareRepository shareRepository;
    private final FileRepository fileRepository;
    private final CurrentUserService currentUserService;
    private final ShareMapper shareMapper;
    private final AppProperties appProperties;

    @Override
    public ShareResponse createShare(
            UUID fileId,
            CreateShareRequest request,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        FileMetadata file = fileRepository
                .findByIdAndOwner(fileId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("File"));

        ShareLink share = ShareLink.builder()
                .token(UUID.randomUUID().toString())
                .owner(owner)
                .file(file)
                .permission(request.getPermission())
                .build();

        if (request.getExpiresInDays() != null) {

            share.setExpiresAt(
                    Instant.now()
                            .plus(request.getExpiresInDays(), ChronoUnit.DAYS));
        }

        share = shareRepository.save(share);

        ShareResponse response = shareMapper.toResponse(share);

        return new ShareResponse(
                response.id(),
                response.token(),
                appProperties.getBaseUrl()
                        + "/api/v1/share/"
                        + response.token(),
                response.permission(),
                response.expiresAt());
    }

    @Override
    public List<ShareResponse> getShares(
            UUID fileId,
            Authentication authentication) {

        throw new UnsupportedOperationException(
                "Share listing will be implemented in the next PR");
    }

    @Override
    public void deleteShare(
            UUID shareId,
            Authentication authentication) {

        throw new UnsupportedOperationException(
                "Share deletion will be implemented in the next PR");
    }
}