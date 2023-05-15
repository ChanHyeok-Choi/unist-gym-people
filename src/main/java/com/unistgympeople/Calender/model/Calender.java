package com.unistgympeople.Calender.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "calender")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Calender {
    @Id
    private String id;
    private int memberid;
    private String time;
    private String event;
    private Integer num;

    public Calender(){}
    public Calender(int memberid, String time, String event, Integer num){
        this.memberid=memberid;
        this.time=time;
        this.event=event;
        this.num=num;
    }

    public int getMemberid() {
        return this.memberid;
    }
    public String getTime(){
        return this.time;
    }
    public String getEvent(){
        return this.event;
    }
    public int getNum() {  return this.num; }
}
