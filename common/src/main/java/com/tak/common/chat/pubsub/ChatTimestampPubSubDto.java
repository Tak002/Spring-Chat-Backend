package com.tak.common.chat.pubsub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class ChatTimestampPubSubDto { // chat-history 에서 서버로 전송되는 메시지의 타임스탬프 DTO
    private UUID id;
    private UUID tempId;
    private String roomId;
    private OffsetDateTime createdAt;
    private OffsetDateTime editedAt;

    public static ChatTimestampPubSubDto of(UUID id, UUID tempId, String roomId, OffsetDateTime createdAt, OffsetDateTime editedAt) {
        return ChatTimestampPubSubDto.builder()
                .id(id)
                .tempId(tempId)
                .roomId(roomId)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .build();
    }
}
