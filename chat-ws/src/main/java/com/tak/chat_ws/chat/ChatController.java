package com.tak.chat_ws.chat;

import com.tak.common.commonDto.pubsub.ChatMessagePubSubDto;
import com.tak.common.commonDto.send.ChatMessageSendDto;
import com.tak.chat_ws.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    @MessageMapping("/chat")
    public void sendMessage(ChatMessageSendDto chatMessageSendDto) {
        redisPublisher.publish(ChatMessagePubSubDto.from(chatMessageSendDto));
    }
}
