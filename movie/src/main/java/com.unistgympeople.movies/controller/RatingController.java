package com.unistgympeople.movies.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.unistgympeople.movies.repository.RatingRepository;
import com.unistgympeople.movies.model.Movie;
import com.unistgympeople.movies.model.User;
import com.unistgympeople.movies.model.Ratings;
import com.unistgympeople.movies.dal.RatingDAL;

@RestController
@RequestMapping (value = "/ratings")
public class RatingController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final RatingDAL ratingDAL;
    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository, RatingDAL ratingDAL) { 
        this.ratingRepository=ratingRepository;
        this.ratingDAL=ratingDAL;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Ratings> getAllRatings() {
        LOG.info("Getting all ratings");
        return ratingRepository.findAll();
    }

    @RequestMapping(value = "/{RatingID}", method = RequestMethod.GET)
    public Ratings getRatings(@PathVariable String ratingId) {
        LOG.info("Getting ratings with Rating ID : {}.", ratingId);
        return ratingRepository.findById(ratingId).orElse(null);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Ratings addNewRatings(@RequestBody Ratings ratings) {
        LOG.info("Saving rating.");
        return ratingRepository.save(ratings);
    }

    @PutMapping("/ratings/{movieId}")
    public Ratings replaceRating(@RequestBody Ratings newRating, @PathVariable String movieId) {
        LOG.info("replacing rating.");
        return ratingRepository.findById(movieId)
                .map(rating -> {
                    rating.setMovieId(newRating.getMovieId());
                    rating.setUserId(newRating.getUserId());
                    rating.setRating(newRating.getRating());
                    rating.setTimestamp(newRating.getTimestamp());
                    return ratingRepository.save(rating);
                })
                .orElseGet(() -> {
                    newRating.setMovieId(movieId);
                    return ratingRepository.save(newRating);
                });
    }

    @DeleteMapping("/ratings/{id}")
    void deleteRating(@PathVariable String ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}
