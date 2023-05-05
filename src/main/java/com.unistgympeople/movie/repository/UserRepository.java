package com.unistgympeople.movie.repository;

import com.unistgympeople.movie.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // User findOne(String userId);
}
