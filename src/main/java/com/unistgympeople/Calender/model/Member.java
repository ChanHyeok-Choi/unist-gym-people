package com.unistgympeople.Calender.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "member")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {
    @Id
    private String id;
    private int memberid;
    private String member_name;

    public Member(){}

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getMember_name() {
        return member_name;
    }


}
