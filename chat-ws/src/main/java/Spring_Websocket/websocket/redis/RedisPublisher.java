package Spring_Websocket.websocket.redis;

import Spring_Websocket.websocket.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final ChannelTopic topic;
    private final RedisTemplate<Object, Object> redisTemplate;

    public void publish(ChatMessageDto message) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
