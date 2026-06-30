package com.cloudnest.trash.service.impl;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.folder.repository.FolderRepository;
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.trash.service.TrashCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashCleanupServiceImpl
        implements TrashCleanupService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final StorageService storageService;

    @Override
    public void cleanup() throws IOException {

        Instant threshold =
                Instant.now().minus(30, ChronoUnit.DAYS);

        List<FileMetadata> files =
                fileRepository.findByDeletedAtBefore(threshold);

        for (FileMetadata file : files) {

            storageService.delete(file.getStorageKey());

            fileRepository.delete(file);
        }

        List<Folder> folders =
                folderRepository.findByDeletedAtBefore(threshold);

        for (Folder folder : folders) {

            folderRepository.delete(folder);
        }

    }
}