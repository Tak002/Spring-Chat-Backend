package com.tak.chat_ws.redis;

import com.tak.chat_common.commonDto.ChatMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final ChannelTopic topic;
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisPublisher(ChannelTopic topic, RedisTemplate<Object, Object> redisTemplate) {
        this.topic = topic;
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
    }

    public void publish(ChatMessageDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
