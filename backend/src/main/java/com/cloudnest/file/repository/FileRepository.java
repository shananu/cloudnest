package com.cloudnest.file.repository;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileMetadata, UUID> {

    List<FileMetadata> findByOwner(User owner);

    List<FileMetadata> findByFolder(Folder folder);

    List<FileMetadata> findByFolderAndOwner(
            Folder folder,
            User owner);

}