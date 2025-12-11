package com.tak.app_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "join_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // meeting_id FK 대신 Long
    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    // user_id FK 대신 Long
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // { "answers": [ [번호, "값"], ... ] } 형태 JSON 문자열
    @Column(name = "answers_json", nullable = false, columnDefinition = "TEXT")
    private String answersJson;

    @Column(name = "answered_at", nullable = false)
    private Instant answeredAt;

    @PrePersist
    void onCreate() {
        if (answeredAt == null) {
            answeredAt = Instant.now();
        }
    }
}
