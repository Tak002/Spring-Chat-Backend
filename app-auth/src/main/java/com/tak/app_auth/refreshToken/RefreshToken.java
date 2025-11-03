package com.tak.app_auth.refreshToken;

import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "refresh_token",
        indexes = {
                @Index(name = "refresh_token_user_id_idx", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // matches BIGINT GENERATED ALWAYS AS IDENTITY
    private Long id;

    // FK: user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // DB에서도 CASCADE지만, Hibernate에게도 힌트 줌
    private AppUser user;

    // CHAR(64) NOT NULL
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    // TIMESTAMPTZ NOT NULL
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    // BOOLEAN NOT NULL DEFAULT FALSE
    @Builder.Default
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    // TIMESTAMPTZ NOT NULL DEFAULT NOW()
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}