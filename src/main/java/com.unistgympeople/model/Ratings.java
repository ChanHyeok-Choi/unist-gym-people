package com.unistgympeople.movies.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
public class Ratings {

<<<<<<< HEAD:src/main/java/com.unistgympeople/model/Ratings.java
    private String userId;
    @Id
    private String movieId;
=======
    
    private String userId;
    private @Id String movieId;
>>>>>>> 7fb3fa8f00387ab48b9a946446dd9d059bfab5c9:movie/src/main/java/com.unistgympeople.movies/model/Ratings.java
    private int Rating;
    private Long Timestamp;
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId=movieId; }
    public int getRating() { return Rating; }
    public void setRating(int Rating) { this.Rating=Rating; }
    public Long getTimestamp() { return Timestamp; }
    public void setTimestamp(Long Timestamp) { this.Timestamp=Timestamp; }
}
