package com.cloudnest.trash.service;

import com.cloudnest.trash.dto.TrashResponse;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;

public interface TrashService {

    TrashResponse getTrash(Authentication authentication);

    void restoreFile(UUID fileId, Authentication authentication);

    void restoreFolder(UUID folderId, Authentication authentication);

    void permanentlyDeleteFile(UUID fileId, Authentication authentication) throws IOException;

    void permanentlyDeleteFolder(UUID folderId, Authentication authentication) throws IOException;

}