package com.cloudnest.trash.service;

import com.cloudnest.trash.dto.TrashResponse;

import java.util.UUID;

import org.springframework.security.core.Authentication;

public interface TrashService {

    TrashResponse getTrash(Authentication authentication);

    void restoreFile(
            UUID fileId,
            Authentication authentication);

    void restoreFolder(
            UUID folderId,
            Authentication authentication);

}