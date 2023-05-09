package com.unistgympeople.realTime.handler;

import com.unistgympeople.realTime.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class RealTimeWebSocketHandler extends TextWebSocketHandler {
    private final UserService userService;
    private int userCount = 0;
    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        this.session = session;
        userCount = getUserCount();
        log.info("New WebSocket connection established. Total users: {}", userCount);
        TextMessage message = new TextMessage(String.valueOf(userCount));
        session.sendMessage(message);
    }

    @Scheduled(fixedDelay = 1000) // 1초마다 사용자 수 갱신
    public void sendUserCount() throws Exception {
        if (session != null) {
            userCount = getUserCount();
            session.sendMessage(new TextMessage(String.valueOf(userCount)));
        }
    }

    public int getUserCount() { return userService.getUser().size(); }
}
