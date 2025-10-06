package com.tak.chat_ws.redis;

import com.tak.chat_common.commonDto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PubSubTestController {
    private final RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String sender, @RequestParam String content, @RequestParam String roomId) {
        ChatMessageDto chat = ChatMessageDto.builder()
                .sender(sender)
                .content(content)
                .roomId(roomId)
                .build();
        redisPublisher.publish(chat);
        return "Message published: " + chat;
    }
}
