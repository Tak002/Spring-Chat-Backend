// src/main/java/Spring_Websocket/websocket/WebSocketSessionLogger.java
package Spring_Websocket.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketSessionLogger {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        int count = sessionCount.incrementAndGet();
        System.out.println("WebSocket 연결됨. 현재 접속자 수: " + count);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        int count = sessionCount.decrementAndGet();
        System.out.println("WebSocket 연결 해제됨. 현재 접속자 수: " + count);
    }
}