package com.unistgympeople.movie.repository;

import com.unistgympeople.movie.model.Ratings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Ratings, String> {
    // Ratings findOne(String userId);
}
