package com.tak.app_media.entity;

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

    // BIGSERIAL PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: "img_777" 같은 비즈니스 키
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "content_type", nullable = false)
    @Builder.Default
    private String contentType = "jpg";

    // THUMBNAIL | PROFILE | ...
    @Column(name = "purpose", nullable = false)
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
