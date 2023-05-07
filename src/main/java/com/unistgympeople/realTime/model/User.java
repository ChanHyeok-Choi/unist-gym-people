package com.unistgympeople.realTime.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL) //include only non_null values
public class User {
    @Id
    private String id;
    private int userId;
    private Date timeStamp;

    public User() {}
    /*public User(String userId, Date timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }*/

    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public Date getTimeStamp() { return timeStamp; }
    public void setTimeStamp(Date timeStamp) { this.timeStamp = timeStamp; }
}
