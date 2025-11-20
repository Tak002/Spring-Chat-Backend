package com.tak.app_service.entity;

import com.tak.common.appUser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "join_answer",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_join_answer_unique",
            columnNames = {"meeting_id", "user_id", "question_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinAnswer {

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

    // question_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private JoinFormQuestion question;

    private String value;

    @Column(name = "answered_at", nullable = false)
    private Instant answeredAt;

    @PrePersist
    void onCreate() {
        if (answeredAt == null) {
            answeredAt = Instant.now();
        }
    }
}
