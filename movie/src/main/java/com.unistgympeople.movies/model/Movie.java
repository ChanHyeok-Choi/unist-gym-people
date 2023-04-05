package com.unistgympeople.movies.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
public class Movie {
    @Id
    private String movieId;
    private String title;
    private String genres;
    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres()
    {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
