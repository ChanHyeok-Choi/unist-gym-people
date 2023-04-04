package com.unistgympeople.movies.dal;

import com.unistgympeople.movies.model.Movie;

import java.util.List;

public interface MovieDAL {

        List<Movie> getAllMovies();

        Movie getMovieById(String movieId);

        Movie addNewMovie(Movie movie);
}

