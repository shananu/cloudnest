package com.cloudnest.file.repository;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileMetadata, UUID> {

    List<FileMetadata> findByOwnerAndDeletedAtIsNull(User owner);
    List<FileMetadata> findByFolderAndDeletedAtIsNull(Folder folder);
    List<FileMetadata> findByFolderAndOwnerAndDeletedAtIsNull(Folder folder, User owner);
    Optional<FileMetadata> findByIdAndOwnerAndDeletedAtIsNull(UUID id, User owner);

    // Deleted files (Trash)
    List<FileMetadata> findByFolderAndDeletedAtIsNotNull(Folder folder);
    List<FileMetadata> findByOwnerAndDeletedAtIsNotNull(User owner);
    Optional<FileMetadata> findByIdAndOwnerAndDeletedAtIsNotNull(UUID id, User owner);

    List<FileMetadata> findByFolder(Folder folder);

}