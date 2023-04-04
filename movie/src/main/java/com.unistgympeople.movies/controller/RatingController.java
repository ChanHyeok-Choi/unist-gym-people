package com.unistgympeople.movies.controller;

import java.util.List;

import com.unistgympeople.movies.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unistgympeople.movies.repository.RatingRepository;
import com.unistgympeople.movies.model.Ratings;

@RestController
@RequestMapping (value = "/ratings")
public class RatingController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) { this.ratingRepository=ratingRepository; }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Ratings> getAllRatings() {
        LOG.info("Getting all ratings");
        return ratingRepository.findAll();
    }

    @RequestMapping(value = "/{MovieID}", method = RequestMethod.GET)
    public Ratings getRatings(@PathVariable int MovieID) {
        LOG.info("Getting ratings with Movie ID : {}.", MovieID);
        return ratingRepository.findRating(MovieID);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Ratings addNewRatings(@RequestBody Ratings ratings) {
        LOG.info("Saving rating.");
        return ratingRepository.save(ratings);
    }
}
