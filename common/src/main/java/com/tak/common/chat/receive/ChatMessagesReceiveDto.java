package com.tak.common.chat.receive;

import lombok.Data;

import java.util.List;

@Data
public class ChatMessagesReceiveDto { // 서버에서 클라이언트로 전송되는 여러 메시지 DTO, 방 초기 입장시 사용
    List<ChatMessageReceiveDto> messages;

    public ChatMessagesReceiveDto(List<ChatMessageReceiveDto> messages) {
        this.messages = messages;
    }
}
