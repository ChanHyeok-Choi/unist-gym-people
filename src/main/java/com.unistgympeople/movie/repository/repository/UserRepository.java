package com.unistgympeople.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movie.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // User findOne(String userId);
}
