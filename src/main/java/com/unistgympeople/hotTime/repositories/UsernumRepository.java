package com.unistgympeople.hotTime.repositories;

import com.unistgympeople.hotTime.model.Usernum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernumRepository extends MongoRepository<Usernum, String> {

}
