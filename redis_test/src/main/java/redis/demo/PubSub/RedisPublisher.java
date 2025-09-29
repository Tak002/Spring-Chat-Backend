package redis.demo.PubSub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final ChannelTopic topic;
    private final RedisTemplate<Object, Object> redisTemplate;

    public void publish(String message) {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
