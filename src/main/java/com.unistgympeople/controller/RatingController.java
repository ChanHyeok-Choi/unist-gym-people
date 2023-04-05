package com.unistgympeople.controller;

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
import com.unistgympeople.repository.RatingRepository;
import com.unistgympeople.model.Ratings;
import com.unistgympeople.dal.RatingDAL;

@RestController
public class RatingController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final RatingDAL ratingDAL;
    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository, RatingDAL ratingDAL) { 
        this.ratingRepository=ratingRepository;
        this.ratingDAL=ratingDAL;
    }

    @GetMapping("/ratings")
    public List<Ratings> getAllRatings() {
        LOG.info("Getting all ratings");
        return ratingRepository.findAll();
    }

<<<<<<< HEAD:src/main/java/com.unistgympeople/controller/RatingController.java
    @RequestMapping(value = "/{MovieID}", method = RequestMethod.GET)
    public Ratings getRatings(@PathVariable String movieId) {
        LOG.info("Getting ratings with User ID : {}.", movieId);
        return ratingRepository.findById(movieId).orElse(null);
=======
    @GetMapping("/ratings/{movieId}")
    public Ratings getRatings(@PathVariable String movieId) {
        LOG.info("Getting ratings with Rating ID : {}.", movieId);
        return ratingRepository.findById(movieId).orElseThrow(() -> new RatingNotFoundException(movieId));
>>>>>>> 7fb3fa8f00387ab48b9a946446dd9d059bfab5c9:movie/src/main/java/com.unistgympeople.movies/controller/RatingController.java
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
