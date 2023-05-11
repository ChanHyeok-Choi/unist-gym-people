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

### Feature 3 : User's Workout Calender

You can execute these CURL commands as follows
```
#Command to show every exercise member has done.
##(Memberid) is Integer value.
curl -X GET http://localhost:8080/Calender/(Memberid)

#Command to show every exercise member has done on certain date.
##(Data) is String value, with format yyyy-mm-dd (ex. 2023-05-07).
curl -X GET http://localhost:8080/Calender/(Memberid)/(Date)

#Command to show total Calorie membe has spent on certain date.
##Expected return value is Integer.
curl -X GET http://localhost:8080/Calender/(Memberid)/(Date)/Calorie

#Command to save the Calender in database
##(event) is String value, and (num) is Integer value.
##Exception thrown when Exercise type is not declared exercise.csv, or num is 0 or negative.
curl -X POST http://localhost:8080/Calender -H 'Content-type:application/json' -d '{"memberid":"(Memberid)", "time":"(Date)", "event":"(event)","num":"(num)"}'

#Command to show all Exercise in database.
curl -X GET http://localhost:8080/Exercise

#Command to show specific Exercise in database.
##(Exercisetype) is String value.
curl -X GET http://localhost:8080/Exercise/(Exercisetype)

#Command to add new Exercise in database
##(exercisetype) is String value and (percalorie) is Integer value.
##Exception is thrown when percalorie is 0 or negative
curl -X POST http://localhost:8080/Exercise -H 'Content-type:application/json' -d '{"exercisetype":"(exercisetype)","percalorie":"(percalorie)"}'
```

---
  
## Part 3 : Achieve more than 90% of branch coverage with your unit tests

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service

### Feature 3 : User's Workout Calender
