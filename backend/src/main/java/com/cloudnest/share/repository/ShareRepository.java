package com.cloudnest.share.repository;

import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.share.entity.ShareLink;
import com.cloudnest.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShareRepository
        extends JpaRepository<ShareLink, UUID> {

    Optional<ShareLink> findByToken(String token);

    List<ShareLink> findByFile(FileMetadata file);

    Optional<ShareLink> findByIdAndOwner(
            UUID id,
            User owner);

}