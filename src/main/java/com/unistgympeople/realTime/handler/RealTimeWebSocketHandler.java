package com.unistgympeople.realTime.handler;

import com.unistgympeople.realTime.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RealTimeWebSocketHandler extends TextWebSocketHandler {
    private final UserService userService;
    private List<WebSocketSession> sessions = new ArrayList<>();

    public void updateUserCount() {
        int userCount = getUserCount();
        String message = Integer.toString(userCount);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Failed to send WebSocket message to session", e);
            }
        }
    }

    public int getUserCount() { return userService.getUserCount(); }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}