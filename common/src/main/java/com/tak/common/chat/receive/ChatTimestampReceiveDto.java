package com.tak.common.chat.receive;

import com.tak.common.chat.pubsub.ChatTimestampPubSubDto;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ChatTimestampReceiveDto { // 서버에서 클라이언트로 전송되는 메시지의 타임스탬프 DTO
    private UUID id;
    private UUID tempId;
    private OffsetDateTime createdAt;
    private OffsetDateTime editedAt;

    public static ChatTimestampReceiveDto from(ChatTimestampPubSubDto pubSubDto) {
        return ChatTimestampReceiveDto.builder()
                .id(pubSubDto.getId())
                .tempId(pubSubDto.getTempId())
                .createdAt(pubSubDto.getCreatedAt())
                .editedAt(pubSubDto.getEditedAt())
                .build();
    }
}
