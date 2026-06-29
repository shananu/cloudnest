package com.cloudnest.share.service.impl;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.share.dto.request.CreateShareRequest;
import com.cloudnest.share.dto.response.ShareResponse;
import com.cloudnest.share.entity.ShareLink;
import com.cloudnest.share.mapper.ShareMapper;
import com.cloudnest.share.repository.ShareRepository;
import com.cloudnest.share.service.ShareService;
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.service.CurrentUserService;
import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.config.AppProperties;
import com.cloudnest.common.util.ShareTokenGenerator;
import com.cloudnest.common.exception.AccessDeniedException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

        private final ShareRepository shareRepository;
        private final FileRepository fileRepository;
        private final CurrentUserService currentUserService;
        private final ShareMapper shareMapper;
        private final AppProperties appProperties;
        private final ShareTokenGenerator shareTokenGenerator;
        private final StorageService storageService;

        @Override
        public ShareResponse createShare(
                        UUID fileId,
                        CreateShareRequest request,
                        Authentication authentication) {

                User owner = currentUserService.getCurrentUser(authentication);

                FileMetadata file = fileRepository
                                .findByIdAndOwnerAndDeletedAtIsNull(fileId, owner)
                                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

                ShareLink share = ShareLink.builder()
                                .token(shareTokenGenerator.generate())
                                .owner(owner)
                                .file(file)
                                .permission(request.getPermission())
                                .build();

                if (request.getExpiresInDays() != null) {
                        share.setExpiresAt(
                                        Instant.now().plus(request.getExpiresInDays(), ChronoUnit.DAYS));
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

                User owner = currentUserService.getCurrentUser(authentication);

                FileMetadata file = fileRepository
                                .findByIdAndOwnerAndDeletedAtIsNull(fileId, owner)
                                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

                return shareRepository.findByFile(file)
                                .stream()
                                .map(share -> {

                                        ShareResponse response = shareMapper.toResponse(share);

                                        return new ShareResponse(
                                                        response.id(),
                                                        response.token(),
                                                        appProperties.getBaseUrl()
                                                                        + "/api/v1/share/"
                                                                        + response.token(),
                                                        response.permission(),
                                                        response.expiresAt());

                                })
                                .toList();
        }

        @Override
        public void deleteShare(
                        UUID shareId,
                        Authentication authentication) {

                User owner = currentUserService.getCurrentUser(authentication);

                ShareLink share = shareRepository
                                .findByIdAndOwner(shareId, owner)
                                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

                shareRepository.delete(share);
        }

        @Override
        public ResponseEntity<Resource> downloadSharedFile(
                        String token) throws IOException {

                ShareLink share = shareRepository.findByToken(token)
                                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

                if (share.getExpiresAt() != null &&
                                share.getExpiresAt().isBefore(Instant.now())) {

                        throw new AccessDeniedException("Share link has expired");
                }

                FileMetadata file = share.getFile();

                Resource resource = storageService.read(file.getStoredFilename());

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" +
                                                                file.getOriginalFilename() +
                                                                "\"")
                                .contentType(
                                                MediaType.parseMediaType(
                                                                file.getContentType()))
                                .body(resource);
        }
}