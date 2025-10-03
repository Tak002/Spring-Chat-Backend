package Spring_Websocket.websocket.redis;

import Spring_Websocket.websocket.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PubSubTestController {
    private final RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String sender, @RequestParam String content, @RequestParam String roomId) {
        ChatMessageDto chat = ChatMessageDto.builder()
                .sender(sender)
                .content(content)
                .roomId(roomId)
                .build();
        redisPublisher.publish(chat);
        return "Message published: " + chat;
    }
}
