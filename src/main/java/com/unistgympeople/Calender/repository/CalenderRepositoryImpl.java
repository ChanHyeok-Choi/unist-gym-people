package com.unistgympeople.Calender.repository;

import com.unistgympeople.Calender.model.Calender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
abstract public class CalenderRepositoryImpl implements CalenderRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Calender> findCalenderByMemberid(int memberid){
        Query query = new Query(Criteria.where("memberid").is(memberid));
        List<Calender> result = mongoTemplate.find(query, Calender.class);
        return result;
    }
    public List<Calender> findCalenderByMemberidAndTime(int memberid, String time){
        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        List<Calender> result = mongoTemplate.find(query, Calender.class);
        return result;
    }

}
