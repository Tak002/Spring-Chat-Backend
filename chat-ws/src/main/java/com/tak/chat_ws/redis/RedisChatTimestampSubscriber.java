package com.tak.chat_ws.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tak.common.chat.pubsub.ChatTimestampPubSubDto;
import com.tak.common.chat.receive.ChatTimestampReceiveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatTimestampSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());
            ChatTimestampPubSubDto timestamp = objectMapper.readValue(body, ChatTimestampPubSubDto.class);

            template.convertAndSend("/topic/" + timestamp.getRoomId(), ChatTimestampReceiveDto.from(timestamp));

            log.info("Received timestamp: {}", timestamp);
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}
