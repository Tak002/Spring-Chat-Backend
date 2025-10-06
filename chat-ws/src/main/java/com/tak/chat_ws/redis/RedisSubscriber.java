package com.tak.chat_ws.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tak.chat_common.commonDto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpMessagingTemplate template;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 온 메시지를 문자열로 변환
            String body = new String(message.getBody());

            // 문자열(JSON)을 ChatMessage로 변환
            ChatMessageDto chatMessage = objectMapper.readValue(body, ChatMessageDto.class);
            String roomId = chatMessage.getRoomId();
            template.convertAndSend("/topic/" + roomId, chatMessage);

            log.info("Received message: {}", chatMessage);
        } catch (Exception e) {
                        log.error("Error processing Redis message", e);
        }
    }
}
