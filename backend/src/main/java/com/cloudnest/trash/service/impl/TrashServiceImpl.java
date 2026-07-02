package com.cloudnest.trash.service.impl;

import com.cloudnest.common.cache.CacheNames;
import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.file.mapper.FileMapper;
import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.folder.mapper.FolderMapper;
import com.cloudnest.folder.repository.FolderRepository;
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.trash.dto.TrashResponse;
import com.cloudnest.trash.service.TrashService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrashServiceImpl implements TrashService {

    private final StorageService storageService;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    private final FileMapper fileMapper;
    private final FolderMapper folderMapper;

    private final CurrentUserService currentUserService;

    @Cacheable(cacheNames = CacheNames.TRASH, key = "#authentication.name")
    @Override
    public TrashResponse getTrash(Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        List<FileResponse> files = fileRepository
                .findByOwnerAndDeletedAtIsNotNull(owner)
                .stream()
                .map(fileMapper::toResponse)
                .toList();

        List<FolderResponse> folders = folderRepository
                .findByOwnerAndDeletedAtIsNotNull(owner)
                .stream()
                .map(folderMapper::toResponse)
                .toList();

        return TrashResponse.builder()
                .files(files)
                .folders(folders)
                .build();
    }

    @Caching(evict = {

            @CacheEvict(cacheNames = CacheNames.TRASH, key = "#authentication.name"),

            @CacheEvict(cacheNames = CacheNames.FILES, key = "#authentication.name")

    })
    @Override
    public void restoreFile(
            UUID fileId,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        FileMetadata file = fileRepository
                .findByIdAndOwnerAndDeletedAtIsNotNull(
                        fileId,
                        owner)
                .orElseThrow(() -> new ResourceNotFoundException("File"));

        file.setDeletedAt(null);

        fileRepository.save(file);
    }

    @Caching(evict = {

            @CacheEvict(cacheNames = CacheNames.TRASH, key = "#authentication.name"),
            @CacheEvict(cacheNames = CacheNames.FILES, key = "#authentication.name"),
            @CacheEvict(cacheNames = CacheNames.FOLDERS, key = "#authentication.name")

    })
    @Override
    public void restoreFolder(
            UUID folderId,
            Authentication authentication) {

        User owner = currentUserService.getCurrentUser(authentication);

        Folder folder = folderRepository
                .findByIdAndOwnerAndDeletedAtIsNotNull(
                        folderId,
                        owner)
                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

        restoreFolderRecursive(folder);
    }

    private void restoreFolderRecursive(
            Folder folder) {

        folder.setDeletedAt(null);

        folderRepository.save(folder);

        List<FileMetadata> files = fileRepository.findByFolderAndDeletedAtIsNotNull(folder);

        for (FileMetadata file : files) {

            if (file.getDeletedAt() != null) {

                file.setDeletedAt(null);

                fileRepository.save(file);
            }
        }

        List<Folder> children = folderRepository.findByParentAndDeletedAtIsNotNull(folder);

        for (Folder child : children) {

            if (child.getDeletedAt() != null) {

                restoreFolderRecursive(child);
            }
        }

    }

    @Caching(evict = {

            @CacheEvict(cacheNames = CacheNames.TRASH, key = "#authentication.name"),
            @CacheEvict(cacheNames = CacheNames.FILES, key = "#authentication.name")

    })
    @Override
    public void permanentlyDeleteFile(
            UUID fileId,
            Authentication authentication) throws IOException {

        User owner = currentUserService.getCurrentUser(authentication);

        FileMetadata file = fileRepository
                .findByIdAndOwnerAndDeletedAtIsNotNull(fileId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("File"));

        storageService.delete(file.getStorageKey());

        fileRepository.delete(file);
    }

    @Caching(evict = {

            @CacheEvict(cacheNames = CacheNames.TRASH, key = "#authentication.name"),
            @CacheEvict(cacheNames = CacheNames.FILES, key = "#authentication.name"),
            @CacheEvict(cacheNames = CacheNames.FOLDERS, key = "#authentication.name")

    })
    @Override
    public void permanentlyDeleteFolder(
            UUID folderId,
            Authentication authentication) throws IOException {

        User owner = currentUserService.getCurrentUser(authentication);

        Folder folder = folderRepository
                .findByIdAndOwnerAndDeletedAtIsNotNull(folderId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

        permanentlyDeleteFolderRecursive(folder);
    }

    private void permanentlyDeleteFolderRecursive(
            Folder folder) throws IOException {

        List<Folder> children = folderRepository.findByParent(folder);

        for (Folder child : children) {
            permanentlyDeleteFolderRecursive(child);
        }

        List<FileMetadata> files = fileRepository.findByFolder(folder);

        for (FileMetadata file : files) {

            storageService.delete(file.getStorageKey());

            fileRepository.delete(file);
        }

        folderRepository.delete(folder);
    }
}