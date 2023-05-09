package com.unistgympeople.chatRoom.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class ChatRoomWebSocketConfig implements WebSocketConfigurer {
    private final ChatRoomWebSocketHandler chatRoomWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRoomWebSocketHandler, "ws/chat").setAllowedOrigins("*");
    }
}