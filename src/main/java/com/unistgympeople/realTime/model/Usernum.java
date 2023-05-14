package com.unistgympeople.realTime.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "usernums")
@JsonInclude(JsonInclude.Include.NON_NULL) //include only non_null values
public class Usernum {

    @Id @Generated
    private String id;
    private String date;
    private String time;
    private int userNumber;

    public Usernum() {}
    /*public User(String userId, Date timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }*/
}
