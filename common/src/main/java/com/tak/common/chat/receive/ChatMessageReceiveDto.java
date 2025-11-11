package com.tak.common.chat.receive;

import com.tak.common.chat.pubsub.ChatMessagePubSubDto;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ChatMessageReceiveDto { // 서버에서 클라이언트로 전송되는 메시지 DTO
    private UUID id;
    private Long senderId;
    private String content;
    private String roomId;
    private OffsetDateTime createdAt;
    private OffsetDateTime editedAt;
    private boolean deleted;

    // 편의 팩토리
    public static ChatMessageReceiveDto of(UUID id, Long senderId, String content, String roomId, OffsetDateTime createdAt, OffsetDateTime editedAt, boolean deleted) {
        return ChatMessageReceiveDto.builder()
                .id(id)
                .senderId(senderId)
                .content(content)
                .roomId(roomId)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .deleted(deleted)
                .build();
    }

    // temp로 설정한 id가 빠르게 chat-history에서 설정한 id로 바뀌어야 함
    public static ChatMessageReceiveDto from(ChatMessagePubSubDto chatMessagePubSubDto) {
        return ChatMessageReceiveDto.builder()
                .id(chatMessagePubSubDto.getTempId()) // id는 tempId로 초기화, 나중에 chat-history에서 설정
                .senderId(chatMessagePubSubDto.getSenderId())
                .content(chatMessagePubSubDto.getContent())
                .roomId(chatMessagePubSubDto.getRoomId())
                .createdAt(null) // createdAt는 나중에 chat-history에서 설정
                .editedAt(null) // editedAt는 나중에 chat-history에서 설정
                .deleted(false) // 기본값은 false
                .build();
    }
}