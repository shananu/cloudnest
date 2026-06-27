package com.cloudnest.folder.repository;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.folder.entity.Folder;
import com.cloudnest.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {

    List<Folder> findByOwner(User owner);
    Optional<Folder> findByIdAndOwner(UUID id, User owner);
    List<Folder> findByParent(Folder parent);
}