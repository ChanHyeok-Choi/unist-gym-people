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
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UserRepository;
import com.unistgympeople.realTime.service.UserService;
import com.unistgympeople.realTime.service.UserServiceImpl;
import com.unistgympeople.realTime.service.UsernumService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.*;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
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
    public void testRegisterWebSocketHandler() throws Exception {
        ChatRoomWebSocketConfig webSocketConfig = new ChatRoomWebSocketConfig(chatRoomWebSocketHandler);
        when(registry.addHandler(any(ChatRoomWebSocketHandler.class), eq("ws/chat"))).thenReturn(registration);
        webSocketConfig.registerWebSocketHandlers(registry);
        verify(registration).setAllowedOrigins("*");

        String roomId = "1";
        String sender = "user1";
        String messageText = "test";

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSender(sender);
        chatMessage.setMessage(messageText);

        WebSocketSession session = mock(WebSocketSession.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        TextMessage textMessage = new TextMessage("{\"type\":\"ENTER\", \"roomId\":\"1\", \"sender\":\"user1\", \"message\":\"something\"}");
        String payload = textMessage.getPayload();
        when(objectMapper.readValue(payload, ChatMessage.class)).thenReturn(chatMessage);
        when(chatService.findRoomById(roomId)).thenReturn(chatRoom);

        ChatRoomWebSocketHandler webSocketHandler = new ChatRoomWebSocketHandler(objectMapper, chatService);
        webSocketHandler.handleTextMessage(session, textMessage);

        verify(objectMapper).readValue(payload, ChatMessage.class);
        verify(chatRoom).handlerActions(session, chatMessage, chatService);
    }

    @Test
    public void testSingleHandleTextMessage() throws Exception {
        String roomId = UUID.randomUUID().toString();

        WebSocketSession session = mock(WebSocketSession.class);

        ChatMessage chatMessageEnter = new ChatMessage();
        chatMessageEnter.setType(ChatMessage.MessageType.ENTER);
        chatMessageEnter.setRoomId(roomId);
        chatMessageEnter.setSender("user1");
        chatMessageEnter.setMessage("user1 enters room " + roomId);

        assertEquals(chatMessageEnter.getType(), ChatMessage.MessageType.ENTER);
        assertEquals(chatMessageEnter.getSender(), "user1");
        assertEquals(chatMessageEnter.getMessage(), "user1 enters room " + roomId);

        TextMessage textMessageEnter = new TextMessage("user1 enters room " + roomId);

        chatService.sendMessage(session, textMessageEnter);
        verify(chatService).sendMessage(session, textMessageEnter);

        doNothing().when(session).sendMessage(any(TextMessage.class));

        ChatMessage chatMessageTalk = mock(ChatMessage.class);
        when(chatMessageTalk.getType()).thenReturn(ChatMessage.MessageType.TALK);
        chatMessageTalk.setType(ChatMessage.MessageType.TALK);
        chatMessageTalk.setRoomId(roomId);
        chatMessageTalk.setSender("user1");
        chatMessageTalk.setMessage("Hello! Nice to meet test!");

        ChatRoom chatRoom = new ChatRoom(roomId, "Test Room");
        chatRoom.handlerActions(session, chatMessageEnter, chatService);
        chatRoom.handlerActions(session, chatMessageTalk, chatService);
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
        assertEquals(createdRoom.getSessions().size(), 0);
    }

    @Test
    public void testFindAllRoom() {
        chatService = new ChatService(objectMapper);
        chatService.init();

        chatService.createRoom("Room 1");
        chatService.createRoom("Room 2");

        List<ChatRoom> rooms = new ArrayList<>();
        rooms.add(new ChatRoom(UUID.randomUUID().toString(), "Room 1"));
        rooms.add(new ChatRoom(UUID.randomUUID().toString(), "Room 2"));

        assertEquals(chatService.findAllRoom().size(), rooms.size());

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

    @Test
    public void testSendMessage() throws IOException {
        chatService = new ChatService(objectMapper);

        String messageText = "Test message";
        TextMessage message = new TextMessage(messageText);

        WebSocketSession session = mock(WebSocketSession.class);

        doNothing().when(session).sendMessage(any(TextMessage.class));
        when(objectMapper.writeValueAsString(message)).thenReturn(messageText);

        chatService.sendMessage(session, message);

        verify(session).sendMessage(any(TextMessage.class));
        verify(objectMapper).writeValueAsString(message);
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
    @Mock
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

    // <--- HotTime Test Code lines --->
    @Mock
    private UserService userService;

    @Mock
    private UsernumService usernumService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testgenerateUserModel() {
        String testid = "userid";
        int testuserid = 123;
        String testtimestamp = "2023-05-14T12:23:12Z";
        User.UserType testusertype1 = User.UserType.ENTER;
        User.UserType testusertype2 = User.UserType.EXIT;
        User usertest1 = new User();
        usertest1.setId(testid);
        usertest1.setUserId(testuserid);
        usertest1.setTimeStamp(testtimestamp);
        usertest1.setUserType(testusertype1);

        assertEquals(usertest1.getId(),testid);
        assertEquals(usertest1.getUserId(),testuserid);
        assertEquals(usertest1.getTimeStamp(),testtimestamp);
        assertEquals(usertest1.getUserType(),testusertype1);
    }

    @Test
    public void testgenerateUsernumModel(){
        String testusernumid = "testid";
        String testdate = "2023-05-14";
        String testtime = "10:08:23";
        int testusernum = 23;
        Usernum usernumtest1 = new Usernum();
        usernumtest1.setId(testusernumid);
        usernumtest1.setDate(testdate);
        usernumtest1.setTime(testtime);
        usernumtest1.setUserNumber(testusernum);

        assertEquals(usernumtest1.getId(),testusernumid);
        assertEquals(usernumtest1.getDate(),testdate);
        assertEquals(usernumtest1.getTime(),testtime);
        assertEquals(usernumtest1.getUserNumber(),testusernum);
    }

    @Test
    public void testUserService() {
        String testId = "1";
        int userId = 1;
        String testTimeStamp = "2023-05-14T12:23:12Z";
        User.UserType testUserType = User.UserType.ENTER;
        User user = new User();
        user.setId(testId);
        user.setUserId(userId);
        user.setTimeStamp(testTimeStamp);
        user.setUserType(testUserType);

        UserServiceImpl userService1 = new UserServiceImpl();
        userService1.setUserRepository(userRepository);
        userService1.setMongoTemplate(mongoTemplate);

        when(userRepository.save(user)).thenReturn(user);

        String id = userService1.save(user);

        verify(userRepository).save(user);
        assertEquals(id, user.getId());

        when(userRepository.findById(testId)).thenReturn(Optional.of(user));
        Optional<User> userById = userService1.getUserById(testId);
        assertTrue(userById.isPresent());
        assertEquals(user, userById.get());

        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> userList = userService1.getUser();
        assertEquals(1, userList.size());
        assertEquals(user, userList.get(0));

        Query enterquery = new Query(Criteria.where("userType").is("ENTER"));
        Query exitquery = new Query(Criteria.where("userType").is("EXIT"));
        when(mongoTemplate.find(enterquery, User.class)).thenReturn(List.of(user));
        when(mongoTemplate.find(exitquery, User.class)).thenReturn(Collections.emptyList());
        assertEquals(1, userService1.getUserCount());
    }

    // <--- HotTime Test Code lines --->

    // Add more test cases for other methods and scenarios...

}

