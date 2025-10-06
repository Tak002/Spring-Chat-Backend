package com.tak.chat_history.chatMessage;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
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
    @GeneratedValue
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false,insertable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private OffsetDateTime editedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted; // 컬럼명은 is_deleted 이지만, 자바 필드는 관례상 'deleted'

    // 편의 팩토리
    public static ChatMessage of(String roomId, String sender, String content) {
        return ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .content(content)
                .deleted(false)
                .build();
    }
}
