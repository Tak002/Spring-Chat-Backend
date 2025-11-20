package com.tak.app_service.entity;

import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @Column(length = 255)
    private String id; // e.g. img_777

    @Column(name = "content_type", nullable = false)
    private String contentType;

    // THUMBNAIL | PROFILE | ...
    private String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
