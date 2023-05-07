package com.unistgympeople.realTime.handler;

import com.unistgympeople.realTime.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final UserRepository userRepository;

    public WebSocketConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new UserCountHandler(userRepository), "/users");
    }
}
