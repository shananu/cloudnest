package com.cloudnest.file.service.impl;

import com.cloudnest.file.dto.response.FileResponse;
import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.file.mapper.FileMapper;
import com.cloudnest.file.repository.FileRepository;
import com.cloudnest.file.service.FileService;
import com.cloudnest.storage.model.StoredFile;
import com.cloudnest.storage.service.StorageService;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageService storageService;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;

    @Override
    public FileResponse upload(
            MultipartFile file,
            Authentication authentication) throws IOException {

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow();

        StoredFile storedFile = storageService.store(file);

        FileMetadata metadata = FileMetadata.builder()
                .originalFilename(file.getOriginalFilename())
                .storedFilename(storedFile.storedFilename())
                .storageKey(storedFile.storageKey())
                .contentType(file.getContentType())
                .size(file.getSize())
                .owner(owner)
                .build();

        metadata = fileRepository.save(metadata);

        return fileMapper.toResponse(metadata);
    }

    @Override
    public List<FileResponse> getMyFiles(Authentication authentication) {

        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow();

        return fileRepository.findByOwner(owner)
                .stream()
                .map(fileMapper::toResponse)
                .toList();
    }

}