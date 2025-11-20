package com.tak.app_service.entity;

import com.tak.app_service.entity.enums.MeetingStatus;
import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "meeting",
       indexes = {
           @Index(name = "ix_meeting_status", columnList = "status"),
           @Index(name = "ix_meeting_host", columnList = "host_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // host_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private AppUser host;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDate date;

    private LocalTime time;

    private String place;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "rules_json")
    private String rulesJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    private Media thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_event_id")
    private Event linkedEvent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status = MeetingStatus.OPEN;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
