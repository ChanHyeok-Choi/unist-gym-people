package com.unistgympeople.movies.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
public class Ratings {

    @Id
    private String userId;
    private String movieId;
    private int Rating;
    private Long Timestamp;
    public int getUserId() { return userId; }
    public void setUserId(String userId) { this.userId=userId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId=movieId; }
    public int Rating() { return Rating; }
    public void setRating(int Rating) { this.Rating=Rating; }
    public Long getTimestamp() { return Timestamp; }
    public void setTimestamp(Long Timestamp) { this.Timestamp=Timestamp; }
}
