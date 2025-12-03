package com.tak.common.chat.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageSendDto { // 클라이언트에서 서버로 전송되는 메시지 DTO
    private String content;
    private Long roomId;
}
