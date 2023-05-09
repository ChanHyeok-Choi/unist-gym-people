package com.unistgympeople.chatRoom.model;

import com.unistgympeople.chatRoom.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + " enter " + chatMessage.getRoomId() + " room.");
        }
        sendMessage(chatMessage, chatService);
/*curl -X POST http://localhost:8080/chat -H "Content-Type: application/json" -d "{ \ "name"
: "ChatRoom1" \ }"
wscat -c ws://localhost:8080/ws/chat
{"type":"ENTER", "roomId":"", "sender":"user1", "message":"asd"}*/
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}
