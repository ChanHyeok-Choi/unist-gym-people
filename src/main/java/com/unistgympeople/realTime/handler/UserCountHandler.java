package com.unistgympeople.realTime.handler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.unistgympeople.realTime.repository.UserRepository;

@Component
public class UserCountHandler extends TextWebSocketHandler {
    private final UserRepository userRepository;

    @Autowired
    public UserCountHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Set to store the WebSocket session being accessed
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    // Methods that run when a message is received from a WebSocket client
    @Override
    @Scheduled(fixedDelay = 1000)
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Count the number of users currently connected
        int userCount = calculateUserCount();

        // Send user count information to all WebSocket sessions
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage("Current user count: " + userCount));
        }
    }

    // Methods that run when a WebSocket client connects
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add a WebSocket session to a Set
        sessions.add(session);

        // Count the number of users currently connected
        int userCount = calculateUserCount();

        // Send user count information to all WebSocket sessions
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage("Current user count: " + userCount));
        }
    }

    // Methods that run when the WebSocket client disconnects
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove a WebSocket session from a Set
        sessions.remove(session);

        // Count the number of users currently connected
        int userCount = calculateUserCount();

        // Send user count information to all WebSocket sessions
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage("Current user count: " + userCount));
        }
    }

    // Methods to count the number of users currently connected
    private int calculateUserCount() {
        return userRepository.findAll().size();
    }
}

