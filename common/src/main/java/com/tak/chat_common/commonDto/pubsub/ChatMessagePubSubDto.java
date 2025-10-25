package com.tak.chat_common.commonDto.pubsub;

import com.tak.chat_common.commonDto.send.ChatMessageSendDto;
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
    private String sender;
    private String content;
    private String roomId;

    public static ChatMessagePubSubDto from(ChatMessageSendDto sendDto) {
        return ChatMessagePubSubDto.builder()
                .sender(sendDto.getSender())
                .content(sendDto.getContent())
                .roomId(sendDto.getRoomId())
                .build();
    }
}
