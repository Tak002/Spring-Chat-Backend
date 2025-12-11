package com.tak.app_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "join_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user_id FK 대신 Long
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // { "questions": [ [번호, "내용"], ... ] } 형태 JSON 문자열
    @Column(name = "questions_json", nullable = false, columnDefinition = "TEXT")
    private String questionsJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
