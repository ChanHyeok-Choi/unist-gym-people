package com.unistgympeople.movies.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movies.model.Ratings;

@Repository
public interface RatingRepository extends MongoRepository<Ratings, String> {
    Ratings findOne(String userId);
}
