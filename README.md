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

mvn package
java -jar target/cse364-project-1.0-SNAPSHOT.jar
```

---

## Part 2 : Implement REST APIs for your three features (90 points, 30 points for each feature)

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service

### Feature 3 : User's Workout Calender

---
  
## Part 3 : Achieve more than 90% of branch coverage with your unit tests

### Feature 1 : Real-Time User Viewer

### Feature 2 : Real-Time Chat Service

### Feature 3 : User's Workout Calender