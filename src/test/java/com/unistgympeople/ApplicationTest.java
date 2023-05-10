package com.unistgympeople;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unistgympeople.chatRoom.controller.ChatController;
import com.unistgympeople.chatRoom.handler.ChatRoomWebSocketHandler;
import com.unistgympeople.chatRoom.model.ChatMessage;
import com.unistgympeople.chatRoom.model.ChatRoom;
import com.unistgympeople.chatRoom.service.ChatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
    // <--- ChatRoom Test Code lines --->

    @Mock
    private ChatService chatService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ChatRoomWebSocketHandler chatRoomWebSocketHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWebSocketConnection() throws Exception {
    }

    @Test
    public void testSingleHandleTextMessage() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        ChatRoom chatRoom = new ChatRoom("1", "Test Room");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.ENTER);
        chatMessage.setRoomId("1");
        chatMessage.setSender("user1");
        chatMessage.setMessage("user1 enters room 1");

        TextMessage textMessage = new TextMessage("user1 enters room 1");
        when(objectMapper.readValue(textMessage.getPayload(), ChatMessage.class)).thenReturn(chatMessage);
        when(chatService.findRoomById("1")).thenReturn(chatRoom);

        assertTrue(chatRoom.getSessions().contains(session));

        chatRoom.handlerActions(session, chatMessage, chatService);
        verify(chatService).sendMessage(session, chatMessage);
    }

    @Test
    public void testCreateRoom() {
        String roomName = "Test Room";
        String roomId = UUID.randomUUID().toString();

        ChatRoom createdRoom = new ChatRoom("1", "Test Room");

        when(chatService.createRoom(roomName)).thenReturn(createdRoom);

        ChatController chatController = new ChatController(chatService);
        ChatRoom result = chatController.createRoom(roomName);

        assertNotNull(result);
        assertEquals(roomId, result.getRoomId());
        assertEquals(roomName, result.getName());
    }

    @Test
    public void testFindAllRoom() {
        List<ChatRoom> rooms = new ArrayList<>();
        rooms.add(new ChatRoom("1", "Room 1"));
        rooms.add(new ChatRoom("2", "Room 2"));

        when(chatService.findAllRoom()).thenReturn(rooms);

        ChatController chatController = new ChatController(chatService);
        List<ChatRoom> result = chatController.findAllRoom();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // <--- ChatRoom Test Code lines --->

    // Add more test cases for other methods and scenarios...

}

