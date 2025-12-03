package com.tak.common.chat.pubsub;

import com.tak.common.chat.send.ChatMessageSendDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePubSubDto { //서버 간의 메시지 전달용
    @Builder.Default
    private UUID tempId = UUID.randomUUID();
    private Long senderId;
    private String content;
    private Long roomId;

    public static ChatMessagePubSubDto from(ChatMessageSendDto sendDto) {
        return ChatMessagePubSubDto.builder()
                .senderId(sendDto.getSenderId())
                .content(sendDto.getContent())
                .roomId(sendDto.getRoomId())
                .build();
    }
}
