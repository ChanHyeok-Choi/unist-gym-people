package com.unistgympeople.hotTime.model;

import com.mongodb.client.result.UpdateResult;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@Document(collection = "usernums")
@JsonInclude(JsonInclude.Include.NON_NULL) //include only non_null values
public class Usernum {

    @Id
    private String id;
    private int usernumId;
    private String date;
    private String time;
    private int num;

    public Usernum() {
    }

    /*
    public Usernum(String date, String time, int num) {
        this.date = date;
        this.time = time;
        this.num = num;
    }*/
    public int getUsernumId() { return usernumId; }
    public void setUsernumId(int id) { this.usernumId = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }
}
