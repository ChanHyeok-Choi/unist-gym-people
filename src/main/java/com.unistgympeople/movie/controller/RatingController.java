package com.unistgympeople.movie.controller;

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
import com.unistgympeople.movie.repository.RatingRepository;
import com.unistgympeople.movie.model.Ratings;

@RestController
public class RatingController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository=ratingRepository;
    }

    @GetMapping("/ratings")
    public List<Ratings> getAllRatings() {
        LOG.info("Getting all ratings");
        return ratingRepository.findAll();
    }

    @GetMapping("/ratings/{movieId}")
    public Ratings getRatings(@PathVariable String movieId) {
        LOG.info("Getting ratings with Rating ID : {}.", movieId);
        return ratingRepository.findById(movieId).orElseThrow(() -> new RatingNotFoundException(movieId));
    }

    @PostMapping("/ratings/")
    public Ratings addNewRatings(@RequestBody Ratings ratings) {
        LOG.info("Saving rating.");
        return ratingRepository.save(ratings);
    }

    @PutMapping("/ratings/{movieId}")
    public Ratings replaceRating(@RequestBody Ratings newRating, @PathVariable String movieId) {
        LOG.info("replacing rating.");
        return ratingRepository.findById(movieId)
                .map(rating -> {
                    rating.setUserId(newRating.getUserId());
                    rating.setMovieId(newRating.getMovieId());
                    rating.setRating(newRating.getRating());
                    rating.setTimestamp(newRating.getTimestamp());
                    return ratingRepository.save(rating);
                })
                .orElseGet(() -> {
                    newRating.setMovieId(movieId);
                    return ratingRepository.save(newRating);
                });
    }

    @DeleteMapping("/ratings/{movieId}")
    void deleteRating(@PathVariable String movieId) {
        ratingRepository.deleteById(movieId);
    }
}
