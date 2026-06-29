package com.cloudnest.trash.service.impl;

import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.file.mapper.FileMapper;
import com.cloudnest.folder.dto.response.FolderResponse;
import com.cloudnest.folder.mapper.FolderMapper;
import com.cloudnest.folder.repository.FolderRepository;
import com.cloudnest.trash.dto.TrashResponse;
import com.cloudnest.trash.service.TrashService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashServiceImpl implements TrashService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    private final FileMapper fileMapper;
    private final FolderMapper folderMapper;

    private final CurrentUserService currentUserService;

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
}