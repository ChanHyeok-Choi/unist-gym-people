package com.unistgympeople.movies.repository;

import java.security.Identity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movies.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findOne(String userId);
}
