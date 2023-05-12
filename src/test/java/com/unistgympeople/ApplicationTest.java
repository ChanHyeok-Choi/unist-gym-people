package com.unistgympeople;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unistgympeople.Calender.Service.CalenderService;
import com.unistgympeople.Calender.controller.CalenderController;
import com.unistgympeople.Calender.model.Calender;
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
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void testSingleHandleTextMessage() throws Exception {
        String roomName = "Test Room";
        String roomId = UUID.randomUUID().toString();

        WebSocketSession session = mock(WebSocketSession.class);
        ChatRoom chatRoom = new ChatRoom(roomId, roomName);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.ENTER);
        chatMessage.setRoomId(roomId);
        chatMessage.setSender("user1");
        chatMessage.setMessage("user1 enters room " + roomId);

        TextMessage textMessage = new TextMessage("user1 enters room " + roomId);
        when(objectMapper.readValue(textMessage.getPayload(), ChatMessage.class)).thenReturn(chatMessage);
        when(chatService.findRoomById(roomId)).thenReturn(chatRoom);

        chatRoom.handlerActions(session, chatMessage, chatService);
        verify(chatService).sendMessage(session, chatMessage);
    }

    @Test
    public void testCreateRoom() {
        String roomName = "Test Room";
        String roomId = UUID.randomUUID().toString();

        ChatRoom createdRoom = new ChatRoom(roomId, roomName);

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

    // <--- Calender Test Code lines --->

    @Mock
    private CalenderService calenderService;

    @Test
    public void testGetCalenderByMember(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;

        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid,time,event,num));

        when(calenderService.getCalenderByMember(memberid)).thenReturn(calenders);

        CalenderController calenderController = new CalenderController(calenderService);
        List<Calender> result = calenderController.getEvents(memberid);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(calenders, result);
    }

    @Test
    public void testCalenderByMemberAndDate(){
        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "Pushup";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = 100;

        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid, time1, event1, num1));
        calenders.add(new Calender(memberid, time1, event2, num2));

        when(calenderService.getCalenderByMemberAndTime(memberid,time1)).thenReturn(calenders);

        CalenderController calenderController = new CalenderController(calenderService);
        List<Calender> result = calenderController.getEventsOnDate(memberid,time1);

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(calenders, result);
    }

    /*
    @Test
    public void testCalenderCalorie(){

        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "Pushup";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = 100;

        Integer answer = 200;
        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid, time1, event1, num1));
        calenders.add(new Calender(memberid, time1, event2, num2));

        when(calenderService.getCalorieByMemberAndTime(memberid,time1)).thenReturn(answer);

        CalenderController calenderController = new CalenderController(calenderService);
        Integer result = calenderController.getCalorieOnDate(memberid,time1);

        assertNotNull(result);
        assertEquals(answer,result);
    }
    */
    // <--- Calender Test Code lines --->

    // Add more test cases for other methods and scenarios...

}

