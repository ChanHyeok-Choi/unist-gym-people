package com.unistgympeople.realTime.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private Date timeStamp;

    public User(String userId, Date timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimeStamp() { return timeStamp; }
    public void setTimeStamp(Date timeStamp) { this.timeStamp = timeStamp; }
}
