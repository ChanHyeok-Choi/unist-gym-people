package com.unistgympeople.movies.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
public class Ratings {

    @Id
    private String ratingId;
    private int Rating;
    private Long Timestamp;
    public String getRatingId() { return ratingId; }
    public void setRatingId(String ratingId) { this.ratingId=ratingId; }
    public int getRating() { return Rating; }
    public void setRating(int Rating) { this.Rating=Rating; }
    public Long getTimestamp() { return Timestamp; }
    public void setTimestamp(Long Timestamp) { this.Timestamp=Timestamp; }
}
