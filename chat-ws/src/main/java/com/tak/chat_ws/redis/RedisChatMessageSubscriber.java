package com.tak.chat_ws.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tak.chat_common.commonDto.pubsub.ChatMessagePubSubDto;
import com.tak.chat_common.commonDto.receive.ChatMessageReceiveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());
            ChatMessagePubSubDto chatMessage = objectMapper.readValue(body, ChatMessagePubSubDto.class);

            // WebSocket 구독자에게 메시지 전달
            String roomId = chatMessage.getRoomId();
            template.convertAndSend("/topic/" + roomId, ChatMessageReceiveDto.from(chatMessage));

            log.info("Received message: {}", chatMessage);
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}
