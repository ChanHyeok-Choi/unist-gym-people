package com.unistgympeople.Calender.repository;

import com.unistgympeople.Calender.model.Calender;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CalenderRepository extends MongoRepository<Calender, String>{
}
