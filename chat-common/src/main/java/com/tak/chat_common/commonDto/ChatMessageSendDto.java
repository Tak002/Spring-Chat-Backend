package com.tak.chat_common.commonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageSendDto {
    private String sender;
    private String content;
    private String roomId;
}
