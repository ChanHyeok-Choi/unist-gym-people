package com.unistgympeople.movie.repository;

import com.unistgympeople.movie.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>{
    // Movie findOne(String movieId);
}
