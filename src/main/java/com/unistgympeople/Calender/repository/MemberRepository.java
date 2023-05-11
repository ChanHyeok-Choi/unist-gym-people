package com.unistgympeople.Calender.repository;

import com.unistgympeople.Calender.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findById();
}
