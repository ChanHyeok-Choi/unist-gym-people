package com.unistgympeople.realTime.repository;

import com.unistgympeople.realTime.model.Usernum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernumRepository extends MongoRepository<Usernum, String> {
}
