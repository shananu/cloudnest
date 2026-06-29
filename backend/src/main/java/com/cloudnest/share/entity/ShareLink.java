package com.cloudnest.share.entity;

import com.cloudnest.common.entity.BaseEntity;
import com.cloudnest.file.entity.FileMetadata;
import com.cloudnest.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(
        name = "share_links",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "token")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShareLink extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharePermission permission;

    private Instant expiresAt;

}