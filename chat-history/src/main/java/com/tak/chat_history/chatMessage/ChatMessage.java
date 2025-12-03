package com.tak.chat_history.chatMessage;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
@SQLDelete(sql = "UPDATE chat_message SET is_deleted = TRUE, edited_at = now() WHERE id = ?")
public class ChatMessage {

    @Id
    @UuidGenerator // Hibernate가 UUID를 생성 (DB의 gen_random_uuid()와 호환)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private Long roomId; // REFERENCES meeting(id)

    @Column(name = "sender_id") // REFERENCES app_user(id)
    private Long senderId; // ON DELETE SET NULL과 정합을 맞추기 위해 nullable 허용

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private OffsetDateTime editedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    // 편의 팩토리
    public static ChatMessage of(Long roomId, Long senderId, String content) {
        return ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .content(content)
                .deleted(false)
                .build();
    }
}
