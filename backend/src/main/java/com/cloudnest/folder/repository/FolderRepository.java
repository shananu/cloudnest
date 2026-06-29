package com.cloudnest.folder.repository;

import com.cloudnest.folder.entity.Folder;
import com.cloudnest.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {

    // Active folders
    List<Folder> findByOwnerAndDeletedAtIsNull(User owner);
    Optional<Folder> findByIdAndOwnerAndDeletedAtIsNull(UUID id, User owner);
    List<Folder> findByParentAndDeletedAtIsNull(Folder parent);

    // Deleted folders (Trash)
    List<Folder> findByOwnerAndDeletedAtIsNotNull(User owner);
    List<Folder> findByParentAndDeletedAtIsNotNull(Folder parent);
    Optional<Folder> findByIdAndOwnerAndDeletedAtIsNotNull(UUID id, User owner);

    List<Folder> findByParent(Folder parent);
}