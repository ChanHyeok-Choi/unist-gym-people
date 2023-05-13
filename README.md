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

mongoimport --db=cse364 --collection=member --authenticationDatabase admin --username admin --password password --type=csv --file=data/member.csv --fields=memberid.int32\(\),member_name.string\(\) --columnsHaveTypes
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
   * (Memberid) is Integer value.
```
curl -X GET http://localhost:8080/Calender/1

//returns All Calenders for memberid 1
```

2. Command to show every exercise member has done on certain date.
   * (Data) is String value, with format yyyy-mm-dd (ex. 2023-05-07).
```
curl -X GET http://localhost:8080/Calender/1/2023-05-08

//returns Calenders for memberid 1 with date 2023-05-08
```

3. Command to show total Calorie membe has spent on certain date.
   * Expected return value is Integer.
```
curl -X GET http://localhost:8080/Calender/1/2023-05-08/Calorie

//returns Total Calorie for memberid 1 with date 2023-05-08
//now for the sample data, the result is (Running) 200 * 10 + (Swimming) 200 * 20 = 6000
```

4. Command to save the Calender in database
   * (event) is String value, and (num) is Integer value.
   * Exception thrown when Exercise type is not declared exercise.csv, or num is 0 or negative.
```
curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Wrong","num":"1"}'
//Throws exception "Exercise type not found!"

curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Swimming","num":"-1"}'
//Throws exception "Number must be positive"

curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"2", "time":"2023-05-11", "event":"Swimming","num":"20"}'
//Saves Database
```

5. Command to show all Exercise in database.
```
curl -X GET http://localhost:8080/Exercise

//returns All Exercises
```

6. Command to show specific Exercise in database.
   * (Exercisetype) is String value.
```
curl -X GET http://localhost:8080/Exercise/Swimming

//returns Database for Swimming
```

7. Command to add new Exercise in database
   * (exercisetype) is String value and (percalorie) is Integer value.
   * Exception is thrown when Exercise type already exists or percalorie is not positive
```
curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"Jumprope","percalorie":"10"}'
//Throws exception "Exercise Already exists!"

curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"0"}'
//Throws exception "PerCalorie must be positive value!"

curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"10"}'
//Saves Database
```

---
  
## Part 3 : Achieve more than 90% of branch coverage with your unit tests

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service
   On the result of `mvn jacoco:report`, there's no branch in ChatRoom Package.
   However, we wrote test codes for mainly operating functions.
1. testSingleHandleTextMessage(): verify that `handleTextMessage()` function works well in [ChatRoomWebSocketHandler.java](src/main/java/com/unistgympeople/chatRoom/handler/ChatRoomWebSocketHandler.java)
2. testCreateRoom(): verify that `createRoom()` function works well in [ChatService.java](src/main/java/com/unistgympeople/chatRoom/service/ChatService.java)
3. testFindAllRoom(): verify that `findAllRoom()` function works well in [ChatService.java](src/main/java/com/unistgympeople/chatRoom/service/ChatService.java)

### Feature 3 : User's Workout Calender
