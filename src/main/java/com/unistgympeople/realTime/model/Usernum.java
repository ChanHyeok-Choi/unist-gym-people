package com.unistgympeople.realTime.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "usernums")
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //include only non_null values
public class Usernum {

    @Id @Generated
    private String id;
    @Getter @Setter @NonNull
    private String date;
    @Getter @Setter @NonNull
    private String time;
    @Getter @Setter @NonNull
    private int userNumber;

    public Usernum() {}
    /*public User(String userId, Date timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }*/
}
