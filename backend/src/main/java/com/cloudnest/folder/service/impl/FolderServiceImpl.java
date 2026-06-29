package com.cloudnest.folder.service.impl;

import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.folder.dto.request.CreateFolderRequest;
import com.cloudnest.folder.dto.request.RenameFolderRequest;
import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.dto.response.FolderTreeResponse;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.folder.mapper.FolderMapper;
import com.cloudnest.folder.repository.FolderRepository;
import com.cloudnest.folder.service.FolderService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.service.CurrentUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    private final CurrentUserService currentUserService;
    private final FileRepository fileRepository;

    @Override
    public FolderResponse create(
            CreateFolderRequest request,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        Folder parent = null;

        if (request.getParentId() != null) {
            parent = folderRepository.findByIdAndOwnerAndDeletedAtIsNull(request.getParentId(), owner)
                    .orElseThrow(() -> new ResourceNotFoundException("Folder"));
        }

        Folder folder = Folder.builder()
                .name(request.getName())
                .owner(owner)
                .parent(parent)
                .build();

        folder = folderRepository.save(folder);

        return folderMapper.toResponse(folder);
    }

    @Override
    public List<FolderResponse> getMyFolders(
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        return folderRepository.findByOwnerAndDeletedAtIsNull(owner)
                .stream()
                .map(folderMapper::toResponse)
                .toList();
    }

    @Override
    public List<FolderTreeResponse> getFolderTree(
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        List<Folder> folders = folderRepository.findByOwnerAndDeletedAtIsNull(owner);

        Map<UUID, FolderTreeResponse> folderMap = new HashMap<>();

        // First pass: create nodes
        for (Folder folder : folders) {
            folderMap.put(folder.getId(), toTreeNode(folder));
        }

        List<FolderTreeResponse> roots = new ArrayList<>();

        // Second pass: build hierarchy
        for (Folder folder : folders) {

            FolderTreeResponse current = folderMap.get(folder.getId());

            if (folder.getParent() == null) {
                roots.add(current);
            } else {

                FolderTreeResponse parent = folderMap.get(folder.getParent().getId());

                if (parent != null) {
                    parent.getChildren().add(current);
                }
            }
        }

        return roots;
    }

    private FolderTreeResponse toTreeNode(
            Folder folder) {

        return FolderTreeResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentId(
                        folder.getParent() == null
                                ? null
                                : folder.getParent().getId())
                .build();
    }

    @Override
    public FolderResponse rename(
            UUID folderId,
            RenameFolderRequest request,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        Folder folder = folderRepository.findByIdAndOwnerAndDeletedAtIsNull(
                folderId,
                owner)
                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

        folder.setName(request.getName());

        folder = folderRepository.save(folder);

        return folderMapper.toResponse(folder);
    }

    @Override
    public void delete(
            UUID folderId,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        Folder folder = folderRepository
                .findByIdAndOwnerAndDeletedAtIsNull(folderId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

        Instant now = Instant.now();

        deleteFolderRecursive(folder, now);
    }

    private void deleteFolderRecursive(
            Folder folder,
            Instant deletedAt) {

        List<Folder> children = folderRepository.findByParentAndDeletedAtIsNull(folder);

        for (Folder child : children) {
            deleteFolderRecursive(child, deletedAt);
        }

        List<FileMetadata> files = fileRepository.findByFolderAndDeletedAtIsNull(folder);

        for (FileMetadata file : files) {
            file.setDeletedAt(deletedAt);
            fileRepository.save(file);
        }

        folder.setDeletedAt(deletedAt);
        folderRepository.save(folder);
    }
}