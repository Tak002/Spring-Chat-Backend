package com.tak.app_service.entity;

import com.tak.app_service.entity.enums.ReportReasonType;
import com.tak.app_service.entity.enums.ReportStatus;
import com.tak.app_service.entity.enums.ReportTargetType;
import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "report",
    indexes = {
        @Index(name = "ux_report_dedup", columnList = "reporter_id,target_type,target_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // reporter_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private AppUser reporter;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ReportTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type", nullable = false)
    private ReportReasonType reasonType;

    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
