# CSE364Project
2023 CSE364 Group 28 Project - Milestone 2

## Part 1 : Create Git branches

You can execute `sh run.sh` as the following:
```
git clone https://github.com/ChanHyeok-Choi/CSE364Project.git
cd CSE364Project

# Create User Administrator MongoDB
mongod --fork --logpath /var/log/mongodb.log
mongosh admin --eval "db.createUser({ user: 'admin', pwd: 'password', roles: ['userAdminAnyDatabase'] })"

# Import data/*.csv to MongoDB
mongoimport --db=cse364 --collection=users --authenticationDatabase admin --username admin --password password --type=csv --file=data/users.csv --fields=userId.int32\(\),timeStamp.string\(\) --columnsHaveTypes
mongoimport --db=cse364 --collection=usernums --authenticationDatabase admin --username admin --password password --type=csv --file=data/usernums.csv --fields=usernumId.int32\(\),date.string\(\),time.string\(\),num.int32\(\) --columnsHaveTypes

mongoimport --db=cse364 --collection=calender --authenticationDatabase admin --username admin --password password --type=csv --file=data/calender.csv --fields=memberid.int32\(\),time.string\(\),event.string\(\),num.int32\(\) --columnsHaveTypes
mongoimport --db=cse364 --collection=exercise --authenticationDatabase admin --username admin --password password --type=csv --file=data/exercise.csv --fields=exercisetype.string\(\),percalorie.int32\(\) --columnsHaveTypes
mvn package

java -jar target/cse364-project-1.0-SNAPSHOT.jar
```

---

## Part 2 : Implement REST APIs for your three features (90 points, 30 points for each feature)

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service

1. Use REST-API for creating a ChatRoom to use real-time chat service.
   ```
   :~/project# curl -X POST http://localhost:8080/chat -H "Content-Type: application/json" -d "{ \ "name" : "ChatRoom1" \ }"
   {"roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","name":"{ \\ name : ChatRoom1 \\ }","sessions":[]}
   ```
2. Open new Terminal and make a `ENTER` request for `user1` and `user2`. Actually, you can use multiple users requests. 
   When requesting a message in the format of `json`, you make sure please adhere to the following format and include the roomId returned after creating the chatRoom. 
   * Terminal 1:
   ```
   :~/project# wscat -c ws://localhost:8080/ws/chat
   Connected (press CTRL+C to quit)
   > {"type":"ENTER", "roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba", "sender":"user1", "message":"something"}
   < {"type":"ENTER","roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","sender":"user1","message":"user1 enter 108fca4d-ccda-44f2-8015-1f0aab43ddba room."}
   ```
   * Terminal 2:
   ```
   :~/project# wscat -c ws://localhost:8080/ws/chat
   Connected (press CTRL+C to quit)
   > {"type":"ENTER", "roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba", "sender":"user2", "message":"something"}
   < {"type":"ENTER","roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","sender":"user2","message":"user2 enter 108fca4d-ccda-44f2-8015-1f0aab43ddba room."}
   ```
   If `user2` enters the ChatRoom on already connected `user1`, you can see an additional message in Terminal 1:
   ```
   < {"type":"ENTER","roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","sender":"user2","message":"user2 enter 108fca4d-ccda-44f2-8015-1f0aab43ddba room."}
   ```
3. Make a `TALK` request for conversation.
   * Terminal 1:
   ```
   > {"type":"TALK", "roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba", "sender":"user2", "message":"Hello!"}
   < {"type":"TALK","roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","sender":"user2","message":"Hello!"}
   ```
   * Terminal 2:
   ```
   < {"type":"TALK","roomId":"108fca4d-ccda-44f2-8015-1f0aab43ddba","sender":"user2","message":"Hello!"}
   ```

### Feature 3 : User's Workout Calender


1. Command to show every exercise member has done.
   * returns All Calenders for memberid 1
      * (Memberid) is Integer value.
```
curl -X GET http://localhost:8080/Calender/1
> [{"id":"6462006b76440abe8d237bb4","memberid":1,"time":"2023-05-08","event":"Running","num":200},
  {"id":"6462006b76440abe8d237bb5","memberid":1,"time":"2023-05-08","event":"Swimming","num":200},
  {"id":"6462006b76440abe8d237bb9","memberid":1,"time":"2023-05-07","event":"Pushup","num":100}]
```

2. Command to show every exercise member has done on certain date.
   * returns Calenders for member id with date.
     * (Data) is String value, with format yyyy-mm-dd (ex. 2023-05-07).
```
curl -X GET http://localhost:8080/Calender/1/2023-05-08
> [{"id":"6462006b76440abe8d237bb4","memberid":1,"time":"2023-05-08","event":"Running","num":200},
   {"id":"6462006b76440abe8d237bb5","memberid":1,"time":"2023-05-08","event":"Swimming","num":200}]
```

3. Command to show total Calorie member has spent on certain date.
   * returns Total Calorie for memberid with date.
     * Expected return value is Integer.
```
curl -X GET http://localhost:8080/Calender/1/2023-05-08/Calorie
> 6000
```

4. Command to save the Calendar in database
   * NULL is thrown when Exercise type is not declared exercise.csv, or num is 0 or negative.
     * (event) is String value, and (num) is Integer value.
```
curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Wrong","num":"1"}'
> (null)

curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Swimming","num":"-1"}'
> (null)

curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Swimming","num":"20"}'
> 646201fbca750a39a015963broot@d49d8dfc5a4a
```

5. Command to show all Exercise in database.
   * Returns All Exercises.
```
curl -X GET http://localhost:8080/Exercise
> [{"id":"6462006bc29958c26ba528c1","exercisetype":"Swimming","percalorie":20},
   {"id":"6462006bc29958c26ba528c2","exercisetype":"Running","percalorie":10},
   {"id":"6462006bc29958c26ba528c3","exercisetype":"dumbbell","percalorie":2},
   {"id":"6462006bc29958c26ba528c4","exercisetype":"Kettlebell","percalorie":3},
   {"id":"6462006bc29958c26ba528c5","exercisetype":"Plank","percalorie":1},
   {"id":"6462006bc29958c26ba528c6","exercisetype":"Pushup","percalorie":2},
   {"id":"6462006bc29958c26ba528c7","exercisetype":"Jumprope","percalorie":1}]
```

6. Command to show specific Exercise in database.
   * Returns Database for certain exercise.
     * (Exercisetype) is String value.
```
curl -X GET http://localhost:8080/Exercise/Swimming
> [{"id":"6462006bc29958c26ba528c1","exercisetype":"Swimming","percalorie":20}]
```

7. Command to add new Exercise in database
   * Null is thrown when Exercise type already exists or percalorie is not positive
     * (exercisetype) is String value and (percalorie) is Integer value.
```
curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"Jumprope","percalorie":"10"}'
> (null)

curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"0"}'
> (null)

curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"10"}'
> 64620358ca750a39a015963croot@d49d8dfc5a4a
```

---
  
## Part 3 : Achieve more than 90% of branch coverage with your unit tests

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service
   On the result of `mvn jacoco:report`, there's only one branch in ChatRoom Package.
   However, we wrote test codes for mainly operating functions.
1. testRegisterWebSocketHandler() & testSingleHandleTextMessage(): verify that `registerWebSocketHandlers()` and `handleTextMessage()` functions work well in [ChatRoomWebSocketConfig.java](src/main/java/com/unistgympeople/chatRoom/handler/ChatRoomWebSocketConfig.java) and [ChatRoomWebSocketHandler.java](src/main/java/com/unistgympeople/chatRoom/handler/ChatRoomWebSocketHandler.java)
2. testCreateRoom(): verify that `createRoom()` function works well in [ChatService.java](src/main/java/com/unistgympeople/chatRoom/service/ChatService.java)
3. testFindAllRoom() & testFindRoomById(): verify that `findAllRoom()` and `findRoomById()` functions work well in [ChatService.java](src/main/java/com/unistgympeople/chatRoom/service/ChatService.java)
4. testSendMessage(): verify that `sendMessage()` function works well in [ChatService.java](src/main/java/com/unistgympeople/chatRoom/service/ChatService.java)

### Feature 3 : User's Workout Calender
   On the result of 'mvn jacoco:report', there are some branches in Calender Package.
1. CalenderModelTest() & CalenderServiceTest() & ExerciseModelTest() & ExerciseServiceTest()
    * verify that `calender`, `calender service`, `exercise`, `exercises service` constructors, getter and setter function work well.
2. GetCalenderByMemberTest() & GetCalenderServiceByMemberTest()
    * verify that `getEvents()`, `getCalenderByMember()` works well in [CalenderController.java](src/main/java/com/unistgympeople/Calender/controller/CalenderController.java) and [CalenderServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/CalenderServiceImpl.java).
3. GetCalenderByMemberAndDateTest() & GetCalenderServiceByMemberAndDateTest()
   * verify that `getEventsOnDate()`, `getCalenderByMemberAndTime()` works well in [CalenderController.java](src/main/java/com/unistgympeople/Calender/controller/CalenderController.java) and [CalenderServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/CalenderServiceImpl.java).
4. GetCalenderCalorieTest() & GetCalenderCalorieEmptyTest() & GetCalenderCalorieErrorTest() & GetCalenderServiceCalorieByMemberAndDateTest() & testGetCalenderServiceCalorieByMemberAndDateError()
    * verify that `getCalorieOnDate()`, `getCalorieByMemberAndTime()` works well in [CalenderController.java](src/main/java/com/unistgympeople/Calender/controller/CalenderController.java) and [CalenderServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/CalenderServiceImpl.java) including all the branch cases.
5. PostCalenderTest() & PostCalenderServiceTest()
    * verify that `save()` works well in [CalenderController.java](src/main/java/com/unistgympeople/Calender/controller/CalenderController.java) and [CalenderServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/CalenderServiceImpl.java) including all the branch cases.
6. GetAllExerciseTest() & GetAllExerciseServiceTest()
    * verify that `getAllExercise()`, `getExercise()` works well in [ExerciseController.java](src/main/java/com/unistgympeople/Calender/controller/ExerciseController.java) and [ExerciseServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/ExerciseServiceImpl.java).
7. GetExerciseByTypeTest() & GetExerciseServiceByTypeTest()
   * verify that `getEvents()`, `getExerciseByExercisetype()` works well in [ExerciseController.java](src/main/java/com/unistgympeople/Calender/controller/ExerciseController.java) and [ExerciseServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/ExerciseServiceImpl.java).
8. PostExerciseTest() & PostExerciseServiceTest()
    * verify that `save()` works well in [ExerciseController.java](src/main/java/com/unistgympeople/Calender/controller/ExerciseController.java) and [ExerciseServiceImpl.java](src/main/java/com/unistgympeople/Calender/Service/ExerciseServiceImpl.java) including all the branch cases.


