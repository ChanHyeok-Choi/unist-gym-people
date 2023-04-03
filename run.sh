#!/bin/bash

git clone https://github.com/ChanHyeok-Choi/CSE364Project.git
cd CSE364Project
mvn install
java -jar target/cse364-project-1.0-SNAPSHOT.jar
