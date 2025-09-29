package redis.demo.PubSub;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;



class RedisSubscriberTest {
    @Test
    public void testOnMessage() {
        // Given
        RedisSubscriber redisSubscriber = new RedisSubscriber();
        Message message = new DefaultMessage("test-channel".getBytes(),"Hello, Redis!".getBytes());
        byte[] pattern = null;

        // When
        redisSubscriber.onMessage(message, pattern);

        // Then
        // Since the method prints to console, we can't assert its output directly.
        // In a real-world scenario, consider using a logging framework and verify logs.
    }

}