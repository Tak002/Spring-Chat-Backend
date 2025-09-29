package Spring_Websocket.websocket;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChatMessageDto {
    private String sender;
    private String content;
    private String roomId;

    public ChatMessageDto() {}
}
