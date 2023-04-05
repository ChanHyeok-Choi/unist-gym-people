package com.unistgympeople.movies.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.unistgympeople.movies.repository.MovieRepository;
import com.unistgympeople.movies.model.Movie;
import com.unistgympeople.movies.dal.MovieDAL;
@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final MovieRepository movieRepository;
    private final MovieDAL movieDAL;

    public MovieController(MovieRepository movieRepository, MovieDAL movieDAL) {

        this.movieRepository = movieRepository;
        this.movieDAL=movieDAL;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Movie> getAllMovies() {
        LOG.info("Getting all movies.");
        return movieRepository.findAll();
    }

    @RequestMapping(value = "/{movieId}", method = RequestMethod.GET)
    public Movie getMovie(@PathVariable String movieId) {
        LOG.info("Getting movie with ID: {}.", movieId);
        return movieRepository.findById(movieId).orElse(null);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Movie addNewMovies(@RequestBody Movie movie) {
        LOG.info("Saving movie.");
        return movieRepository.save(movie);
    }

    @PutMapping("/movies/{movieId}")
    public Movie replaceMovie(@RequestBody Movie newMovie, @PathVariable String movieId) {
        LOG.info("replacing movie.");
        return movieRepository.findById(movieId)
                .map(movie -> {
                    movie.setTitle(newMovie.getMovieId());
                    movie.setGenres(newMovie.getGenres());
                    return movieRepository.save(movie);
                })
                .orElseGet(() -> {
                    newMovie.setMovieId(movieId);
                    return movieRepository.save(newMovie);
                });
    }

    @DeleteMapping("/movies/{id}")
    void deleteMovie(@PathVariable String movieId) {
        movieRepository.deleteById(movieId);
    }
}

