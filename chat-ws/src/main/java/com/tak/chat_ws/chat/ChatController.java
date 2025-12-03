package com.tak.chat_ws.chat;

import com.tak.common.chat.pubsub.ChatMessagePubSubDto;
import com.tak.common.chat.send.ChatMessageSendDto;
import com.tak.chat_ws.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    @MessageMapping("/chat")
    public void sendMessage(ChatMessageSendDto chatMessageSendDto,  SimpMessageHeaderAccessor headerAccessor) {
        Object userIdAttr = headerAccessor.getSessionAttributes().get("userId");
        if (userIdAttr == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        Long userId = (userIdAttr instanceof Long)
                ? (Long) userIdAttr
                : Long.valueOf(userIdAttr.toString());

        redisPublisher.publish(ChatMessagePubSubDto.from(chatMessageSendDto,userId));
    }
}
