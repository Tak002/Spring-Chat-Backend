package com.tak.chat_ws.redis;

import com.tak.common.chat.pubsub.ChatMessagePubSubDto;
import com.tak.common.chat.send.ChatMessageSendDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final ChannelTopic topic;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPublisher(ChannelTopic chatMessageTopic, RedisTemplate<String, Object> redisTemplate) {
        this.topic = chatMessageTopic;
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageSendDto.class));
    }

    public void publish(ChatMessagePubSubDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
