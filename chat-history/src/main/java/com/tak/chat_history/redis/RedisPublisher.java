package com.tak.chat_history.redis;

import com.tak.common.commonDto.pubsub.ChatTimestampPubSubDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final ChannelTopic topic;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPublisher(ChannelTopic chatTimestampTopic, RedisTemplate<String, Object> redisTemplate) {
        this.topic = chatTimestampTopic;
        this.redisTemplate = redisTemplate;
    }

    public void publish(ChatTimestampPubSubDto message) { // timestamp 전송
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
