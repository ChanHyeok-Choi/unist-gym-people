package com.unistgympeople;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unistgympeople.repository.MovieRepository;
import com.unistgympeople.model.Movie;
import com.unistgympeople.dal.MovieDAL;

@RestController
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final MovieRepository movieRepository;
    private final MovieDAL movieDAL;

    public MovieController(MovieRepository movieRepository, MovieDAL movieDAL) {

        this.movieRepository = movieRepository;
        this.movieDAL=movieDAL;
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        LOG.info("Getting all ");
        return movieRepository.findAll();
    }

    @GetMapping("/ratings/{movieId}")
    public Movie getMovie(@PathVariable String movieId) {
        LOG.info("Getting movie with ID: {}.", movieId);
        return movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));
    }

    @PostMapping("/ratings/")
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

    @DeleteMapping("/movies/{movieId}")
    void deleteMovie(@PathVariable String movieId) {
        movieRepository.deleteById(movieId);
    }
}

