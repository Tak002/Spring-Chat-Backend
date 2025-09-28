package redis.demo.PubSub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PubSubTestController {
    private final RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String message) {
        redisPublisher.publish(message);
        return "Message published: " + message;
    }
}
