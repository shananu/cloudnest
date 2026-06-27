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
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final StorageService storageService;

    @Override
    public FolderResponse create(
            CreateFolderRequest request,
            Authentication authentication) {

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Folder parent = null;

        if (request.getParentId() != null) {
            parent = folderRepository.findByIdAndOwner(request.getParentId(), owner)
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

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        return folderRepository.findByOwner(owner)
                .stream()
                .map(folderMapper::toResponse)
                .toList();
    }

    @Override
    public List<FolderTreeResponse> getFolderTree(
            Authentication authentication) {

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        List<Folder> folders = folderRepository.findByOwner(owner);

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

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Folder folder = folderRepository.findByIdAndOwner(
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
            Authentication authentication) throws IOException {

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Folder folder = folderRepository.findByIdAndOwner(
                folderId,
                owner)
                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

        deleteFolderRecursive(folder);
    }

    private void deleteFolderRecursive(
            Folder folder) throws IOException {

        // Delete child folders first
        List<Folder> children = folderRepository.findByParent(folder);

        for (Folder child : children) {
            deleteFolderRecursive(child);
        }

        // Delete files inside this folder
        List<FileMetadata> files = fileRepository.findByFolder(folder);

        for (FileMetadata file : files) {

            storageService.delete(
                    file.getStorageKey());

            fileRepository.delete(file);
        }

        // Finally delete current folder
        folderRepository.delete(folder);
    }
}