package com.cloudnest.file.service.impl;

import com.cloudnest.common.exception.AccessDeniedException;
import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.file.dto.request.RenameFileRequest;
import com.cloudnest.file.dto.response.DownloadFileResponse;
import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.mapper.FileMapper;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.file.service.FileService;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.folder.repository.FolderRepository;
import com.cloudnest.storage.model.StoredFile;
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.repository.UserRepository;
import com.cloudnest.user.service.CurrentUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.UUID;
import java.util.List;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

        private final StorageService storageService;
        private final FileRepository fileRepository;
        private final FileMapper fileMapper;
        private final CurrentUserService currentUserService;
        private final FolderRepository folderRepository;

        @Override
        public FileResponse upload(
                        MultipartFile file,
                        UUID folderId,
                        Authentication authentication) throws IOException {

                User owner = currentUserService.getCurrentUser(authentication);

                Folder folder = null;

                if (folderId != null) {

                        folder = folderRepository.findByIdAndOwner(
                                        folderId,
                                        owner)
                                        .orElseThrow(() -> new ResourceNotFoundException("Folder"));
                }

                StoredFile storedFile = storageService.store(file);

                FileMetadata metadata = FileMetadata.builder()
                                .originalFilename(file.getOriginalFilename())
                                .storedFilename(storedFile.storedFilename())
                                .storageKey(storedFile.storageKey())
                                .contentType(file.getContentType())
                                .size(file.getSize())
                                .owner(owner)
                                .folder(folder)
                                .build();

                metadata = fileRepository.save(metadata);

                return fileMapper.toResponse(metadata);
        }

        @Override
        public List<FileResponse> getMyFiles(Authentication authentication) {

                User owner = currentUserService.getCurrentUser(authentication);

                return fileRepository.findByOwner(owner)
                                .stream()
                                .map(fileMapper::toResponse)
                                .toList();
        }

        @Override
        public DownloadFileResponse download(
                        UUID fileId,
                        Authentication authentication) throws IOException {

                User owner = currentUserService.getCurrentUser(authentication);

                FileMetadata metadata = getOwnedFile(fileId, owner);

                Resource resource = storageService.read(metadata.getStoredFilename());

                return new DownloadFileResponse(
                                resource,
                                metadata.getOriginalFilename(),
                                metadata.getContentType());
        }

        private FileMetadata getOwnedFile(UUID fileId, User owner) {

                FileMetadata metadata = fileRepository.findById(fileId)
                                .orElseThrow(() -> new ResourceNotFoundException("File"));

                if (!metadata.getOwner().getId().equals(owner.getId())) {
                        throw new AccessDeniedException();
                }

                return metadata;
        }

        @Override
        public void delete(
                        UUID fileId,
                        Authentication authentication) throws IOException {

                User owner = currentUserService.getCurrentUser(authentication   );

                FileMetadata metadata = getOwnedFile(fileId, owner);

                storageService.delete(metadata.getStoredFilename());

                fileRepository.delete(metadata);
        }

        @Override
        public FileResponse rename(
                        UUID fileId,
                        RenameFileRequest request,
                        Authentication authentication) {

                User owner = currentUserService.getCurrentUser(authentication);

                FileMetadata metadata = getOwnedFile(fileId, owner);
                metadata.setOriginalFilename(request.getFilename());
                metadata = fileRepository.save(metadata);

                return fileMapper.toResponse(metadata);
        }

        @Override
        public List<FileResponse> getFilesInFolder(
                        UUID folderId,
                        Authentication authentication) {

                User owner = currentUserService.getCurrentUser(authentication);

                Folder folder = folderRepository.findByIdAndOwner(
                                folderId,
                                owner)
                                .orElseThrow(() -> new ResourceNotFoundException("Folder"));

                return fileRepository.findByFolderAndOwner(
                                folder,
                                owner)
                                .stream()
                                .map(fileMapper::toResponse)
                                .toList();
        }

}