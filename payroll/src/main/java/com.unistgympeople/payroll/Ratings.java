package com.unistgympeople.payroll;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Ratings {

    @Id
    private int UserID;
    private int MovieID;
    private int Rating;
    private Long Timestamp;
    public int getUserID() { return UserID; }
    public void setUserID(int UserID) { this.UserID=UserID; }
    public int getMovieID() { return MovieID; }
    public void setMovieID(int MovieID) { this.MovieID=MovieID; }
    public int Rating() { return Rating; }
    public void setRating(int Rating) { this.Rating=Rating; }
    public Long getTimestamp() { return Timestamp; }
    public void setTimestamp(Long Timestamp) { this.Timestamp=Timestamp; }
}
