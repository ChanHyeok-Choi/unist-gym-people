package com.unistgympeople;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unistgympeople.Calender.Service.CalenderService;
import com.unistgympeople.Calender.Service.CalenderServiceImpl;
import com.unistgympeople.Calender.Service.ExerciseService;
import com.unistgympeople.Calender.Service.ExerciseServiceImpl;
import com.unistgympeople.Calender.controller.CalenderController;
import com.unistgympeople.Calender.controller.ExerciseController;
import com.unistgympeople.Calender.model.Calender;
import com.unistgympeople.Calender.model.Exercise;
import com.unistgympeople.Calender.repository.CalenderRepository;
import com.unistgympeople.Calender.repository.ExerciseRepository;
import com.unistgympeople.chatRoom.controller.ChatController;
import com.unistgympeople.chatRoom.handler.ChatRoomWebSocketConfig;
import com.unistgympeople.chatRoom.handler.ChatRoomWebSocketHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.unistgympeople.chatRoom.model.ChatMessage;
import com.unistgympeople.chatRoom.model.ChatRoom;
import com.unistgympeople.chatRoom.service.ChatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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
    @Mock
    private ChatRoomWebSocketHandler chatRoomWebSocketHandler;
    @Mock
    private WebSocketHandlerRegistration registration;
    @Mock
    private WebSocketHandlerRegistry registry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterWebSocketHandlers() {
        ChatRoomWebSocketConfig webSocketConfig = new ChatRoomWebSocketConfig(chatRoomWebSocketHandler);
        when(registry.addHandler(any(ChatRoomWebSocketHandler.class), eq("ws/chat"))).thenReturn(registration);
        webSocketConfig.registerWebSocketHandlers(registry);
        verify(registration).setAllowedOrigins("*");
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

    @Test
    public void testFindRoomById() {
        chatService = new ChatService(objectMapper);
        chatService.init();
        ChatRoom chatRoom = chatService.createRoom("Test Room");
        ChatRoom retrievedRoom = chatService.findRoomById(chatRoom.getRoomId());
        assertEquals(chatRoom, retrievedRoom);
    }

    // <--- ChatRoom Test Code lines --->
    // <--- Calender Test Code lines --->

    @Mock
    private CalenderRepository calenderRepository;
    @Mock
    private CalenderService calenderService;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseService exerciseService;
    @Autowired
    private MongoTemplate mongoTemplate;
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
    public void testGetCalenderServiceByMember(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;
        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid,time,event,num));
        when(calenderRepository.findCalenderByMemberid(memberid)).thenReturn(calenders);
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        List<Calender> result = calenderService1.getCalenderByMember(memberid);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(calenders,result);
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

    @Test
    public void testGetCalenderServiceByMemberAndDate(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;
        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid,time,event,num));
        when(calenderRepository.findCalenderByMemberidAndTime(memberid,time)).thenReturn(calenders);
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        List<Calender> result = calenderService1.getCalenderByMemberAndTime(memberid,time);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(calenders,result);
    }
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
    /*@Test
    public void testCalenderServiceCalorie(){
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
        when(calenderRepository.findCalenderByMemberidAndTime(memberid,time1)).thenReturn(calenders);

        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        Integer result = calenderService1.getCalorieByMemberAndTime(memberid, time1);
        assertNotNull(result);
        assertEquals(answer,result);
    }*/

    @Test
    public void testPostCalender(){

        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "NotExists";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Calender test1 = new Calender(memberid, time1, event2, num1);
        Calender test2 = new Calender(memberid, time1, event1, num1);
        Calender test3 = new Calender(memberid, time1, event2, num2);
        CalenderController calenderController = new CalenderController(calenderService);
        String output1 = calenderController.save(test2);
        String output2 = calenderController.save(test3);
        String output3 = calenderController.save(test1);
        assertEquals(null,output1);
        assertEquals(null,output2);
        assertEquals(test1.getId(),output3);
    }

    @Test
    public void testPostCalenderService(){
        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "NotExists";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Calender test1 = new Calender(memberid, time1, event2, num1);
        Calender test2 = new Calender(memberid, time1, event1, num1);
        Calender test3 = new Calender(memberid, time1, event2, num2);
        String output1 = calenderService.save(test2);
        String output2 = calenderService.save(test3);
        String output3 = calenderService.save(test1);
        assertEquals(null,output1);
        assertEquals(null,output2);
        assertEquals(test1.getId(),output3);
    }

    @Test
    public void testCalorieCalenderEmpty(){
        CalenderController calenderController = new CalenderController(calenderService);
        Integer result = calenderController.getCalorieOnDate(1,"2023-05-07");
        Integer answer = 0;
        assertEquals(answer, result);
    }
    @Test
    public void testCalorieCalender(){
        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "NotExists";
        Integer num1 = 50;
        List<Calender> calenders = new ArrayList<>();
        calenders.add(new Calender(memberid, time1, event1, num1));

        when(calenderService.getCalorieByMemberAndTime(memberid,time1)).thenReturn(-1);
        CalenderController calenderController = new CalenderController(calenderService);
        Integer result = calenderController.getCalorieOnDate(memberid,time1);
        Integer answer = -1;

        assertEquals(answer, result);
    }
    @Test
    public void testGetAllExercise(){
        List<Exercise> exercises= new ArrayList<>();
        exercises.add(new Exercise("TestExercise1",100));
        exercises.add(new Exercise("TestExercise2",50));
        when(exerciseService.getExercise()).thenReturn(exercises);

        ExerciseController exerciseController = new ExerciseController(exerciseService);
        List<Exercise> result = exerciseController.getAllExercise();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(exercises, result);
    }
    @Test
    public void testGetAllExerciseService(){
        List<Exercise> exercises= new ArrayList<>();
        Exercise exercise1 = new Exercise("TestExercise1",100);
        Exercise exercise2 = new Exercise("TestExercise2",50);
        exercises.add(exercise1);
        exercises.add(exercise2);
        when(exerciseRepository.findAll()).thenReturn(exercises);
        ExerciseService exerciseService1 = new ExerciseServiceImpl(exerciseRepository,mongoTemplate);
        List<Exercise> result = exerciseService1.getExercise();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(exercises, result);
    }
    @Test
    public void testGetExerciseByType(){
        List<Exercise> exercises= new ArrayList<>();
        exercises.add(new Exercise ("TestExercise1",100));
        when(exerciseService.getExerciseByExercisetype("TestExercise1")).thenReturn(exercises);

        ExerciseController exerciseController = new ExerciseController(exerciseService);
        List<Exercise> result = exerciseController.getEvents("TestExercise1");

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(exercises, result);
    }
    @Test
    public void testPostExercise(){

        int memberid = 1;
        String event1 = "NewExercise";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Exercise test1 = new Exercise(event1,num1);
        Exercise test2 = new Exercise(event2,num1);
        Exercise test3 = new Exercise("NewerExercise",num2);
        ExerciseController exerciseController = new ExerciseController(exerciseService);
        String output1 = exerciseController.save(test1);
        String output2 = exerciseController.save(test2);
        String output3 = exerciseController.save(test3);
        assertEquals(test1.getId(),output1);
        assertEquals(null,output2);
        assertEquals(null,output3);
    }
    @Test
    public void testPostExerciseService(){

        int memberid = 1;
        String event1 = "NewExercise";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Exercise test1 = new Exercise(event1,num1);
        Exercise test2 = new Exercise(event2,num1);
        Exercise test3 = new Exercise("NewerExercise",num2);
        String output1 = exerciseService.save(test1);
        String output2 = exerciseService.save(test2);
        String output3 = exerciseService.save(test3);
        assertEquals(test1.getId(),output1);
        assertEquals(null,output2);
        assertEquals(null,output3);
    }
    
    // <--- Calender Test Code lines --->

    // Add more test cases for other methods and scenarios...

}

