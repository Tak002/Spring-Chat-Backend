package Spring_Websocket.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto) {
        // 메시지 처리 로직
        System.out.println("chatMessageDto = " + chatMessageDto);
        return chatMessageDto;
    }
}
