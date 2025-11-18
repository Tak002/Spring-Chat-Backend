package com.tak.common.appUser;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CITEXT (대소문자 구분 없음)
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "CITEXT")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.active;   // 'active' | 'suspended' | 'deleted'

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.user;           // 'user' | 'admin'

    @Column(name = "nickname", nullable = false, columnDefinition = "CITEXT")
    private String nickname;

    @Column(name = "department")
    private String department;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;// TEXT로 확장

    @Column(name = "birth_date")
    private LocalDate birthDate; // 생년월일

    @Column(name = "visibility")
    private String visibility;               // e.g. "PUBLIC" (확장 가능, enum 도입 전까지 TEXT로 유지)

    @Column(name = "profile_image_id")
    private String profileImageId;           // FK -> media.id (별도 엔티티 매핑 전까지 문자열)

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    // --- Enum 정의 (DB CHECK 값과 동일한 소문자) ---
    public enum Status { active, suspended, deleted }
    public enum Role { user, admin }
}
