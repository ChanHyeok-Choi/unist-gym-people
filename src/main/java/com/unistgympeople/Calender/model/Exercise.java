package com.unistgympeople.Calender.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "exercise")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Exercise {
    @Id
    private String id;
    private String exercisetype;
    private Integer percalorie;
    public Exercise(){};
    public Exercise(String exercisetype, Integer percalorie)
    {
        this.exercisetype = exercisetype;
        this.percalorie = percalorie;
    }

    public String getexercisetype() {
        return exercisetype;
    }

    public Integer getpercalorie() {
        return percalorie;
    }
}
