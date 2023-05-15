package com.unistgympeople.realTime.handler;

import com.unistgympeople.realTime.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RealTimeWebSocketHandler extends TextWebSocketHandler {
    private final UserService userService;
    private List<WebSocketSession> sessions = new ArrayList<>();
}
