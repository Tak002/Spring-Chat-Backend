package com.tak.app_service.entity;

import com.tak.app_service.entity.enums.MeetingMemberRole;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "meeting_member",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_meeting_member_unique",
            columnNames = {"meeting_id", "user_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // meeting_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    // user_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingMemberRole role = MeetingMemberRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingMemberState state = MeetingMemberState.PENDING;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Column(name = "decided_at")
    private Instant decidedAt;

    @PrePersist
    void onCreate() {
        if (requestedAt == null) {
            requestedAt = Instant.now();
        }
    }
}
