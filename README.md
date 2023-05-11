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

1. Use RESTapi for creating a ChatRoom to use real-time chat service.
   ```
   :~/project# curl -X POST http://localhost:8080/chat -H "Content-Type: application/json" -d "{ \ "name" : "ChatRoom1" \ }"
   {"roomId":"a7220487-0395-4cf4-97c6-92f489062e6c","name":"{ \\ name : ChatRoom1 \\ }","sessions":[]}
   ```
2. Open new Terminal and make a ENTER request for `user1` and `user2`. Actually, you can use multiple users requests. 
   When requesting a message in the format of `json`, you make sure Please adhere to the following format and include the roomId returned when creating the chatRoom. 
   * Terminal 1:
   ```
   :~/project# wscat -c ws://localhost:8080/ws/chat
   Connected (press CTRL+C to quit)
   > {"type":"ENTER", "roomId":"a7220487-0395-4cf4-97c6-92f489062e6c", "sender":"user1", "message":"something"}
   ```
   * Terminal 2:
   ```
   :~/project# wscat -c ws://localhost:8080/ws/chat
   Connected (press CTRL+C to quit)
   > {"type":"ENTER", "roomId":"a7220487-0395-4cf4-97c6-92f489062e6c", "sender":"user2", "message":"something"}
   ```
   If `user2` enters the ChatRoom, you can see the below message in Terminal 1.
   ```
   > 
   ```
3. Make a TALK request for conversation.
   ```
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
   * Exception is thrown when percalorie is 0 or negative
```
curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"0"}'
//Throws exception "PerCalorie must be positive value!"

curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"TestExercise","percalorie":"10"}'
//Saves Database
```

---
  
## Part 3 : Achieve more than 90% of branch coverage with your unit tests

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service

### Feature 3 : User's Workout Calender
