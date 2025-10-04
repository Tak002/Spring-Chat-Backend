package Spring_Websocket.websocket.chat;

import Spring_Websocket.websocket.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;
    private final RedisPublisher redisPublisher;
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatMessageDto chatMessageDto) {
        // 메시지 처리 로직
//        System.out.println("ChatMessageDto = " + chatMessageDto);
        redisPublisher.publish(chatMessageDto);
//        template.convertAndSend("/topic/" + roomId, chatMessageDto);
    }
}
