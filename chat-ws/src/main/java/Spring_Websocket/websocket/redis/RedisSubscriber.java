package Spring_Websocket.websocket.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RedisSubscriber implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("Received Redis message: {}", message.toString());
    }
}
