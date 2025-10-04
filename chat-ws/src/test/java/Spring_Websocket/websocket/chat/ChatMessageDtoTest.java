package Spring_Websocket.websocket.chat;

import org.junit.jupiter.api.Test;

class ChatMessageDtoTest {
    @Test
    public void testChatMessageDto() {
        ChatMessageDto chatMessage = ChatMessageDto.builder()
                .sender("Alice")
                .content("Hello, World!")
                .roomId("room1")
                .build();

        System.out.println("chatMessage = " + chatMessage);
    }
}