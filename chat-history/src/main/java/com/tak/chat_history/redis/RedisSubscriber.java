package com.tak.chat_history.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tak.common.commonDto.pubsub.ChatMessagePubSubDto;
import com.tak.common.commonDto.pubsub.ChatTimestampPubSubDto;
import com.tak.chat_history.chatMessage.ChatMessage;
import com.tak.chat_history.chatMessage.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper;
    private final RedisPublisher redisPublisher;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 온 메시지를 문자열로 변환
            String body = new String(message.getBody());

            // 문자열(JSON)을 ChatMessage로 변환
            ChatMessagePubSubDto chatMessage = objectMapper.readValue(body, ChatMessagePubSubDto.class);

            log.info("Received message: {}", chatMessage);
            ChatMessage savedChat = chatMessageRepository.save(ChatMessage.of(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage.getContent()));
            redisPublisher.publish(ChatTimestampPubSubDto.of(savedChat.getId(),chatMessage.getTempId(), savedChat.getRoomId(), savedChat.getCreatedAt(),savedChat.getEditedAt()));
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}
