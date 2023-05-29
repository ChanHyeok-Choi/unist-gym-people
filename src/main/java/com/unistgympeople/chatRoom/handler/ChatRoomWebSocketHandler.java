package com.unistgympeople.chatRoom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unistgympeople.chatRoom.model.ChatMessage;
import com.unistgympeople.chatRoom.model.ChatRoom;
import com.unistgympeople.chatRoom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatRoomWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private static List<WebSocketSession> list = new ArrayList<>();

//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("{}", payload);
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//
//        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
//        chatRoom.handlerActions(session, chatMessage, chatService);
//    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);

        log.info(session + " client connected");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " client disconnected");
        list.remove(session);
    }
}