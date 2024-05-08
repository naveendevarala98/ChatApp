package com.socialmedia.chat.config;

import com.socialmedia.chat.payload.ChatMessage;
import com.socialmedia.chat.payload.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("user disconnected: {}",username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }

    }

}