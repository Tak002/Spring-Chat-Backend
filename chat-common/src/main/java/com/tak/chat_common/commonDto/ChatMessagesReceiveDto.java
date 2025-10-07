package com.tak.chat_common.commonDto;

import lombok.Data;

import java.util.List;

@Data
public class ChatMessagesReceiveDto {
    List<ChatMessageReceiveDto> messages;

    public ChatMessagesReceiveDto(List<ChatMessageReceiveDto> messages) {
        this.messages = messages;
    }
}
