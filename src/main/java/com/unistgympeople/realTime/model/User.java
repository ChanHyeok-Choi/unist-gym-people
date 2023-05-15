package com.unistgympeople.realTime.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    public enum UserType{
        ENTER, EXIT
    }
    @Id
    private String id;
    private int userId;
    private String timeStamp;
    private UserType userType;

    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}
