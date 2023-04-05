package com.unistgympeople.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // User findOne(String userId);
}
