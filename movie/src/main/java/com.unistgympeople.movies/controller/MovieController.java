package com.unistgympeople.movies.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unistgympeople.movies.repository.MovieRepository;
import com.unistgympeople.movies.model.Movie;
import com.unistgympeople.movies.dal.MovieDAL;
@RestController
@RequestMapping(value = "/users")
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
        LOG.info("Getting user with ID: {}.", movieId);
        return movieRepository.findbyId(movieId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Movie addNewMovies(@RequestBody Movie movie) {
        LOG.info("Saving movie.");
        return movieRepository.save(movie);
    }

}
