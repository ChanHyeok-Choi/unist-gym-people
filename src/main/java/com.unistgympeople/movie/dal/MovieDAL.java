package com.unistgympeople.movie.dal;

import com.unistgympeople.movie.model.Movie;

import java.util.List;

public interface MovieDAL {

        List<Movie> getAllMovies();

        Movie getMovieById(String movieId);

        Movie addNewMovie(Movie movie);
}

