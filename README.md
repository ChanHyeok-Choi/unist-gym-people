# CSE364Project
2023 CSE364 Group 28 Project

## Part 1 : Environment Setup
1. Unzip file on Blackboard, named "CSE364Project_Group28"
2. Make docker container and run
3. Inside of docker container, run "run.sh"  
  
---
"run.sh" have next four commands.
* git clone https://github.com/ChanHyeok-Choi/CSE364Project.git
* cd CSE364Project
* mvn install
* java -jar target/cse364-project-1.0-SNAPSHOT.jar
---

## Part 2 : Test Examples
Q1. curl -X GET http://localhost:8080/employees  
A1. [{"id":1,"name":"Bilbo Baggins","role":"burglar"},{"id":2,"name":"Frodo Baggins","role":"thief"}]  
  
Q2. curl -X GET http://localhost:8080/employees/1  
A2. {"id":1,"name":"Bilbo Baggins","role":"burglar"}  
  
Q3. curl -X GET http://localhost:8080/employees/2  
A3. {"id":2,"name":"Frodo Baggins","role":"thief"}  
  
Q4. curl -X GET http://localhost:8080/employees/3  
A4. Could not find employee 3  
  
Q5. curl -X POST http://localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "gardener"}'  
A5. {"id":3,"name":"Samwise Gamgee","role":"gardener"}  
  
Q6. curl -X PUT http://localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'  
A6. {"id":3,"name":"Samwise Gamgee","role":"ring bearer"}  
  
## Part 3 : Test Examples
Q1.  
A1.  
