package com.tak.chat_common.commonDto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ChatMessageReceiveDto {
    private UUID id;
    private String sender;
    private String content;
    private String roomId;
    private OffsetDateTime createdAt;
    private OffsetDateTime editedAt;
    private boolean deleted;

    // 편의 팩토리
    public static ChatMessageReceiveDto of(UUID id, String sender, String content, String roomId, OffsetDateTime createdAt, OffsetDateTime editedAt, boolean deleted) {
        return ChatMessageReceiveDto.builder()
                .id(id)
                .sender(sender)
                .content(content)
                .roomId(roomId)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .deleted(deleted)
                .build();
    }
}