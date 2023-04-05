package com.unistgympeople.movies.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movies.model.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>{
    // Movie findOne(String movieId);
}
