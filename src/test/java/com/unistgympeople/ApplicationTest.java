package com.unistgympeople;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;
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
import com.unistgympeople.realTime.controller.UserController;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UserRepository;
import com.unistgympeople.realTime.repository.UsernumRepository;
import com.unistgympeople.realTime.service.UserService;
import com.unistgympeople.realTime.service.UserServiceImpl;
import com.unistgympeople.realTime.service.UsernumService;
import com.unistgympeople.realTime.service.UsernumServiceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
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

        List<ChatRoom> rooms = new ArrayList<>();
        rooms.add(new ChatRoom(UUID.randomUUID().toString(), "Room 1"));

        assertEquals(chatService.findAllRoom().size(), rooms.size());

        ChatController chatController = new ChatController(chatService);
        List<ChatRoom> result = chatController.findAllRoom();

        assertNotNull(result);
        assertEquals(1, result.size());
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
    public void CalenderModelTest(){
        Calender calender1 = new Calender();
        Calender calender2 = new Calender(1,"2023-05-14","Pushup",10);
        assertEquals(calender1.getClass(),Calender.class);
        assertEquals(calender2.getMemberid(),1);
        assertEquals(calender2.getTime(),"2023-05-14");
        assertEquals(calender2.getEvent(),"Pushup");
        assertEquals(calender2.getNum(),10);
        calender1.setId("TEST");
        calender1.setMemberid(1);
        calender1.setEvent("NewEvent");
        calender1.setTime("2023-05-15");
        calender1.setNum(10);
        assertEquals(calender1.getId(),"TEST");
        assertEquals(calender1.getMemberid(),1);
        assertEquals(calender1.getTime(),"2023-05-15");
        assertEquals(calender1.getEvent(),"NewEvent");
        assertEquals(calender1.getNum(),10);
    }
    @Test
    public void CalenderServiceTest(){
        CalenderService calenderService1 = new CalenderServiceImpl();
        CalenderService calenderService2 = new CalenderServiceImpl(calenderRepository);
        CalenderService calenderService3 = new CalenderServiceImpl(calenderRepository, mongoTemplate);

        assertEquals(calenderService1.getClass(),CalenderServiceImpl.class);
        assertEquals(calenderService2.getClass(),CalenderServiceImpl.class);
        assertEquals(calenderService3.getClass(),CalenderServiceImpl.class);
    }
    @Test
    public void GetCalenderByMemberTest(){
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
    public void GetCalenderServiceByMemberTest(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;
        Calender calender = new Calender(memberid,time,event,num);
        Calender calender1 = new Calender(1,"2023-05-13","Pushup",10);
        List<Calender> calenders = new ArrayList<>();
        calenders.add(calender);
        calenders.add(calender1);
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        when(mongoTemplate.find(query,Calender.class)).thenReturn(calenders);

        List<Calender> result = calenderService1.getCalenderByMember(memberid);

        assertNotNull(result);
        assertEquals(2,result.size());
    }

    @Test
    public void GetCalenderByMemberAndDateTest(){
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
    public void GetCalenderServiceByMemberAndDateTest(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;
        Calender calender = new Calender(memberid,time,event,num);
        Calender calender1 = new Calender(1,"2023-05-13","Pushup",10);
        List<Calender> calenders = new ArrayList<>();
        calenders.add(calender);
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);

        when(calenderRepository.save(calender)).thenReturn(calender);
        when(calenderRepository.save(calender1)).thenReturn(calender1);
        String id1 = calenderService1.save(calender);
        String id2 = calenderService1.save(calender1);
        assertEquals(id1, calender.getId());
        assertEquals(id2, calender1.getId());

        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        when(mongoTemplate.find(query,Calender.class)).thenReturn(calenders);

        List<Calender> result = calenderService1.getCalenderByMemberAndTime(memberid,time);

        assertNotNull(result);
        assertEquals(1,result.size());
    }


    @Test
    public void GetCalenderCalorieTest(){

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

    @Test
    public void GetCalenderCalorieEmptyTest(){
        CalenderController calenderController = new CalenderController(calenderService);
        Integer result = calenderController.getCalorieOnDate(1,"2023-05-07");
        Integer answer = 0;
        assertEquals(answer, result);
    }
    @Test
    public void GetCalenderCalorieErrorTest(){
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
    public void GetCalenderServiceCalorieByMemberAndDateTest(){
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Pushup";
        Integer num = 50;
        Calender calender = new Calender(memberid,time,event,num);
        Calender calender1 = new Calender(1,"2023-05-12","Jumprope",10);
        List<Calender> calenders = new ArrayList<>();
        calenders.add(calender);
        calenders.add(calender1);
        Integer answer =110;
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);

        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        when(mongoTemplate.find(query,Calender.class)).thenReturn(calenders);

        Exercise exercise1 = new Exercise("Pushup",2);
        Exercise exercise2 = new Exercise("Jumprope",1);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("exercisetype").is("Pushup"));
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("exercisetype").is("Jumprope"));
        when(mongoTemplate.findOne(query1,Exercise.class)).thenReturn(exercise1);
        when(mongoTemplate.findOne(query2,Exercise.class)).thenReturn(exercise2);

        Integer result = calenderService1.getCalorieByMemberAndTime(memberid,time);

        assertNotNull(result);
        assertEquals(answer, result);
    }

    @Test
    public void testGetCalenderServiceCalorieByMemberAndDateError()
    {
        int memberid = 1;
        String time = "2023-05-12";
        String event = "Error";
        Integer num = 50;
        Calender calender = new Calender(memberid,time,event,num);
        List<Calender> calenders = new ArrayList<>();
        calenders.add(calender);
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        when(mongoTemplate.find(query,Calender.class)).thenReturn(calenders);

        Query query3 = new Query();
        query3.addCriteria(Criteria.where("exercisetype").is("Error"));
        when(mongoTemplate.findOne(query3,Exercise.class)).thenReturn(null);
        Integer answer = -1;
        Integer result = calenderService1.getCalorieByMemberAndTime(memberid,time);

        assertNotNull(result);
        assertEquals(answer, result);
    }

    @Test
    public void PostCalenderTest(){

        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "NotExists";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Calender test1 = new Calender(memberid, time1, event2, num1);
        test1.setId("TEST");
        Calender test2 = new Calender(memberid, time1, event1, num1);
        Calender test3 = new Calender(memberid, time1, event2, num2);
        CalenderController calenderController = new CalenderController(calenderService);
        when(calenderService.save(test1)).thenReturn(test1.getId());
        when(calenderService.save(test2)).thenReturn(null);
        when(calenderService.save(test3)).thenReturn(null);
        String output1 = calenderController.save(test2);
        String output2 = calenderController.save(test3);
        String output3 = calenderController.save(test1);
        assertNotNull(output3);
        assertEquals(null,output1);
        assertEquals(null,output2);
        assertEquals(test1.getId(),output3);
    }
    @Test
    public void PostCalenderServiceTest(){
        int memberid = 1;
        String time1 = "2023-05-12";
        String event1 = "NotExists";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        CalenderService calenderService1 = new CalenderServiceImpl(calenderRepository,mongoTemplate);
        Calender test1 = new Calender(memberid, time1, event2, num1);
        test1.setId("TEST");
        Calender test2 = new Calender(memberid, time1, event1, num1);
        Calender test3 = new Calender(memberid, time1, event2, num2);
        Exercise exercise = new Exercise("Jumprope",1);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("exercisetype").is("Jumprope"));
        when(mongoTemplate.findOne(query1, Exercise.class)).thenReturn(exercise);
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("exercisetype").is("NotExists"));
        when(mongoTemplate.findOne(query2, Exercise.class)).thenReturn(null);
        when(calenderRepository.save(test1)).thenReturn(test1);
        when(calenderRepository.save(test2)).thenReturn(test2);
        when(calenderRepository.save(test3)).thenReturn(test3);
        String answer = test1.getId();
        String id1 = calenderService1.save(test1);
        String id2 = calenderService1.save(test2);
        String id3 = calenderService1.save(test3);
        assertNotNull(id1);
        assertEquals(answer,id1);
        assertEquals(null,id2);
        assertEquals(null,id3);
    }

    @Test
    public void ExerciseModelTest(){
        Exercise exercise1 = new Exercise();
        Exercise exercise2 = new Exercise("NewExercise",10);
        Integer answer = 10;
        assertEquals(exercise1.getClass(),Exercise.class);
        assertEquals(exercise2.getClass(),Exercise.class);
        assertEquals(exercise2.getexercisetype(),"NewExercise");
        assertEquals(exercise2.getpercalorie(), answer);
        exercise1.setId("TEST");
        exercise1.setExercisetype("NewExercise");
        exercise1.setPercalorie(10);
        assertEquals(exercise1.getId(),"TEST");
        assertEquals(exercise1.getexercisetype(),"NewExercise");
        assertEquals(exercise1.getpercalorie(),answer);
    }

    @Test
    public void ExerciseServiceTest(){
        ExerciseService exerciseService1 = new ExerciseServiceImpl();
        ExerciseService exerciseService2 = new ExerciseServiceImpl(exerciseRepository);
        ExerciseService exerciseService3 = new ExerciseServiceImpl(exerciseRepository, mongoTemplate);

        assertEquals(exerciseService1.getClass(),ExerciseServiceImpl.class);
        assertEquals(exerciseService2.getClass(),ExerciseServiceImpl.class);
        assertEquals(exerciseService3.getClass(),ExerciseServiceImpl.class);
    }

    @Test
    public void GetAllExerciseTest(){
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
    public void GetAllExerciseServiceTest(){
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
    public void GetExerciseByTypeTest(){
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
    public void GetExerciseServiceByTypeTest(){

        String event2 = "Jumprope";
        Integer num1 = 50;
        Exercise exercise = new Exercise(event2,num1);
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("exercisetype").is(event2));
        when(mongoTemplate.find(query1, Exercise.class)).thenReturn(exercises);
        ExerciseService exerciseService1 = new ExerciseServiceImpl(exerciseRepository,mongoTemplate);
        List<Exercise> result = exerciseService1.getExerciseByExercisetype(event2);
        assertNotNull(result);
        assertEquals(result.size(),1);
        assertEquals(result,exercises);
    }

    @Test
    public void PostExerciseTest(){
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
    public void PostExerciseServiceTest(){

        String event1 = "NewExercise";
        String event2 = "Jumprope";
        Integer num1 = 50;
        Integer num2 = -1;
        Exercise test1 = new Exercise(event1,num1);
        Exercise test2 = new Exercise(event2,num1);
        Exercise test3 = new Exercise("NewerExercise",num2);
        ExerciseService exerciseService1 = new ExerciseServiceImpl(exerciseRepository,mongoTemplate);
        Exercise exercise = new Exercise("Jumprope",1);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("exercisetype").is("Jumprope"));
        when(mongoTemplate.findOne(query1, Exercise.class)).thenReturn(exercise);
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("exercisetype").is("NewExercise"));
        when(mongoTemplate.findOne(query2, Exercise.class)).thenReturn(null);
        Query query3 = new Query();
        query3.addCriteria(Criteria.where("exercisetype").is("NewerExercise"));
        when(mongoTemplate.findOne(query3, Exercise.class)).thenReturn(null);
        when(exerciseRepository.save(test1)).thenReturn(test1);
        when(exerciseRepository.save(test2)).thenReturn(test2);
        when(exerciseRepository.save(test3)).thenReturn(test3);
        String answer = test2.getId();
        String id1 = exerciseService1.save(test1);
        String id2 = exerciseService1.save(test2);
        String id3 = exerciseService1.save(test3);
        assertEquals(null,id1);
        assertEquals(answer,id2);
        assertEquals(null,id3);
    }

    // <--- Calender Test Code lines --->

    // <--- HotTime Test Code lines --->
    @Mock
    private UserService userService;

    @Mock
    private UsernumService usernumService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UsernumRepository usernumRepository;

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
        String testId2 = "2";
        int userId2 = 2;
        String testTimeStamp2 = "2023-05-15T12:23:12Z";
        User.UserType testUserType2 = User.UserType.ENTER;
        User user2 = new User();
        user2.setId(testId2);
        user2.setUserId(userId2);
        user2.setTimeStamp(testTimeStamp2);
        user2.setUserType(testUserType2);

        UserServiceImpl userService1 = new UserServiceImpl();
        userService1.setUserRepository(userRepository);
        userService1.setMongoTemplate(mongoTemplate);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(user2)).thenReturn(user2);

        String id = userService1.save(user);
        String id2 = userService1.save(user2);

        verify(userRepository).save(user);
        assertEquals(id, user.getId());

        verify(userRepository).save(user2);
        assertEquals(id2, user2.getId());

        Query getmaxquery = new Query();
        getmaxquery.limit(1).with(Sort.by(Sort.Direction.DESC,"userId"));
        when(mongoTemplate.find(getmaxquery,User.class)).thenReturn(List.of(user2));
        int max_id = userService1.getMaxId();
        assertEquals(1,max_id);

        Query idquery = new Query(Criteria.where("userId").is(userId));
        when(mongoTemplate.findOne(idquery,User.class)).thenReturn(user);
        Optional<User> userByuserId1 = userService1.getUserById(userId);
        Optional<User> userByuserId2 = userService1.getUserById(13);
        assertTrue(userByuserId1.isPresent());
        assertFalse(userByuserId2.isPresent());
        assertEquals(user,userByuserId1.get());

        when(userRepository.findById(testId)).thenReturn(Optional.of(user));
        Optional<User> userById = userService1.getUserById(testId);
        assertTrue(userById.isPresent());
        assertEquals(user, userById.get());

        when(userRepository.findAll()).thenReturn(List.of(user,user2));
        List<User> userList = userService1.getUser();
        assertEquals(2, userList.size());
        assertEquals(user, userList.get(0));

        Query enterquery = new Query(Criteria.where("userType").is("ENTER"));
        Query exitquery = new Query(Criteria.where("userType").is("EXIT"));
        when(mongoTemplate.find(enterquery, User.class)).thenReturn(List.of(user,user2));
        when(mongoTemplate.find(exitquery, User.class)).thenReturn(Collections.emptyList());
        assertEquals(2, userService1.getUserCount());

        Query voidquery = new Query();
        when(mongoTemplate.find(voidquery,User.class)).thenReturn(List.of(user,user2));
        List<User> getalluser = userService1.getAllUser();
        assertEquals(2,getalluser.size());

        Query updatequery = new Query(Criteria.where("userId").is(userId2));
        Update update = new Update().set("timeStamp",user2.getTimeStamp())
                .set("userType",user2.getUserType());
        when(mongoTemplate.updateFirst(updatequery,update,User.class)).thenReturn(null);
        UpdateResult updateResult = userService1.updateUserById(userId2,user2);
        assertNull(updateResult);
    }

    @Test
    public void testUsernumService() {
        String testid = "1";
        String testdate = "2023-05-15";
        String testtime = "16:51:22";
        int usernumber = 3;
        Usernum usernum = new Usernum();
        usernum.setId(testid);
        usernum.setDate(testdate);
        usernum.setTime(testtime);
        usernum.setUserNumber(usernumber);

        UsernumServiceImpl usernumService1 = new UsernumServiceImpl();
        usernumService1.setUsernumRepository(usernumRepository);
        usernumService1.setMongoTemplate2(mongoTemplate);

        when(usernumRepository.save(usernum)).thenReturn(usernum);
        String id = usernumService1.save(usernum,usernumber);
        verify(usernumRepository).save(usernum);
        assertEquals(id, usernum.getId());

        when(usernumRepository.findById(id)).thenReturn(Optional.of(usernum));
        Optional<Usernum> usernumFindById = usernumService1.getUsernumById(testid);
        assertTrue(usernumFindById.isPresent());
        assertEquals(usernum,usernumFindById.get());

        Query voidquery = new Query();
        when(mongoTemplate.find(voidquery,Usernum.class)).thenReturn(List.of(usernum));
        List<Usernum> getallusernumList = usernumService1.getAllUsernum();
        assertEquals(1, getallusernumList.size());
        assertEquals(usernum, getallusernumList.get(0));

        Query usernumbydatequery = new Query(Criteria.where("date").is(testdate));
        when(mongoTemplate.find(usernumbydatequery,Usernum.class)).thenReturn(List.of(usernum));
        List<Usernum> getnumberbydateList = usernumService1.getUsernumByDate(testdate);
        assertEquals(new ArrayList<>(),getnumberbydateList);

        String testid2 = "2";
        String testdate2 = "2023-05-15";
        String testtime2 = "17:13:21";
        int usernumber2 = 6;
        Usernum usernum2 = new Usernum();
        usernum2.setId(testid2);
        usernum2.setDate(testdate2);
        usernum2.setTime(testtime2);
        usernum2.setUserNumber(usernumber2);

        when(usernumRepository.save(usernum2)).thenReturn(usernum2);
        String id2 = usernumService1.save(usernum2,usernumber2);
        verify(usernumRepository).save(usernum2);
        assertEquals(id2, usernum2.getId());

        Query usernumbydatequery2 = new Query(Criteria.where("date").is(testdate));
        when(mongoTemplate.find(usernumbydatequery,Usernum.class)).thenReturn(List.of(usernum,usernum2));
        List<Usernum> getnumberbydateList2 = usernumService1.getUsernumByDate(testdate);
        assertEquals(1,getnumberbydateList2.size());
        assertEquals(usernum2,getnumberbydateList2.get(0));
    }

    @Test
    public void testUserController() {
        UserController userController = new UserController();
        userController.setUserController(userService);
        userController.setUsernumController(usernumService);

        String testId = "1";
        int userId = 1;
        String testTimeStamp = "2023-05-14T12:23:12Z";
        User.UserType testUserType = User.UserType.ENTER;
        User user = new User();
        user.setId(testId);
        user.setUserId(userId);
        user.setTimeStamp(testTimeStamp);
        user.setUserType(testUserType);

        when(userService.save(user)).thenReturn(testId);
        when(userService.getUserById(testId)).thenReturn(Optional.of(user));
        ResponseEntity<User> resresponse = userController.saveUser(user);
        assertNotNull(resresponse);
        assertEquals(user,resresponse.getBody());

        String testnumid = "1";
        String testdate = "2023-05-15";
        String testtime = "18:27:23";
        when(userService.getUserCount()).thenReturn(1);
        Usernum countusernum = new Usernum();
        countusernum.setId(testnumid);
        countusernum.setDate(testdate);
        countusernum.setTime(testtime);
        countusernum.setUserNumber(1);
        when(usernumService.save(countusernum,1)).thenReturn(countusernum.getId());
        when(usernumService.getUsernumById(countusernum.getId())).thenReturn(Optional.of(countusernum));
        ResponseEntity<Long> resusernum = userController.getUserCount();
        assertNotNull(resusernum);
        assertEquals(1,resusernum.getBody().intValue());

        when(usernumService.getAllUsernum()).thenReturn(List.of(countusernum));
        List<Usernum> getallusernums = userController.getAllUsernum();
        assertEquals(1,getallusernums.size());
        assertEquals(countusernum,getallusernums.get(0));

        when(userService.getAllUser()).thenReturn(List.of(user));
        List<User> getallusers = userController.getAllUser();
        assertEquals(1, getallusers.size());
        assertEquals(user,getallusers.get(0));

        when(usernumService.getUsernumByDate(testdate)).thenReturn(List.of(countusernum));
        List<Usernum> getdateusernums = userController.getHotdate(testdate);
        assertEquals(1, getdateusernums.size());
        assertEquals(countusernum,getdateusernums.get(0));

        int getuser_id = Integer.parseInt(testId);
        when(userService.getUserById(getuser_id)).thenReturn(Optional.of(user));
        ResponseEntity<User> getbyiduser = userController.getUser(testId);
        assertNotNull(getbyiduser);
        assertEquals(user,getbyiduser.getBody());

        int update_id = Integer.parseInt(testId);
        when(userService.updateUserById(update_id,user)).thenReturn(null);
        ResponseEntity<User> updatetest = userController.updateUser(testId,user);
        assertNotNull(updatetest);
        assertEquals(user,updatetest.getBody());
    }
    // <--- HotTime Test Code lines --->

    // Add more test cases for other methods and scenarios...

}

