package com.cloudnest.folder.service;

import com.cloudnest.folder.dto.request.CreateFolderRequest;
import com.cloudnest.folder.dto.request.RenameFolderRequest;
import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.dto.response.FolderTreeResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FolderService {

        FolderResponse create(
                        CreateFolderRequest request,
                        Authentication authentication);

        List<FolderResponse> getMyFolders(
                        Authentication authentication);

        List<FolderTreeResponse> getFolderTree(
                        Authentication authentication);

        FolderResponse rename(
                        UUID folderId,
                        RenameFolderRequest request,
                        Authentication authentication);

        void delete(
                        UUID folderId,
                        Authentication authentication) throws IOException;
}