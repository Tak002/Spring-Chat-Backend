package Spring_Websocket.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatMessageDto {
    private String sender;
    private String content;
    private String roomId;

}
