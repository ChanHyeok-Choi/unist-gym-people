package com.unistgympeople.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.model.Ratings;

@Repository
public interface RatingRepository extends MongoRepository<Ratings, String> {
    // Ratings findOne(String userId);
}
