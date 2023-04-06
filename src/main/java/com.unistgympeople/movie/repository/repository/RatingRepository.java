package com.unistgympeople.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movie.model.Ratings;

@Repository
public interface RatingRepository extends MongoRepository<Ratings, String> {
    // Ratings findOne(String userId);
}
