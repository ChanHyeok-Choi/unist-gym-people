package com.unistgympeople.hotTime.service;

import com.unistgympeople.hotTime.repositories.UsernumRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.stereotype.Service;

import com.unistgympeople.hotTime.model.Usernum;
import com.unistgympeople.hotTime.model.Result;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
@Service
public class UsernumServiceImpl implements UsernumService{

    @Autowired
    private UsernumRepository usernumRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Usernum usernum) {
        usernum.setUsernumId(getMaxId()+1);
        usernum.setDate(usernum.getDate());
        usernum.setTime(usernum.getTime());
        usernum.setNum(usernum.getNum());
        return usernumRepository.save(usernum).getId();
    }
    @Override
    public List<Usernum> getUsernum(){
        return usernumRepository.findAll();
    }

    @Override
    public Optional<Usernum> getUsernumByDate(String date) {
        Query query = new Query(Criteria.where("date").is(date));
        Usernum usernum = mongoTemplate.findOne(query, Usernum.class);
        if(usernum == null) {
            return Optional.empty();
        }
        return Optional.of(usernum);
    }

    @Override
    public Optional<Usernum> getUsernumById(int id) {
        Query query = new Query(Criteria.where("usernumId").is(id));
        Usernum usernum = mongoTemplate.findOne(query, Usernum.class);
        if(usernum == null) {
            return Optional.empty();
        }
        return Optional.of(usernum);
    }
    @Override
    public Optional<Usernum> getUsernumById(String id) {
        return usernumRepository.findById(id);
    }

    @Override
    public Result<List<Object>> getAvgUsernum(String date){
        MatchOperation getSpecificDateUsernum = Aggregation.match(
            Criteria.where("date").is(date)
        );

        GroupOperation avgUsernum = Aggregation.group("$date").avg("$num").as("avg_num");

        MatchOperation filterAvgNum = Aggregation.match(
                Criteria.where("num").gte("avg_num")
        );

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.ASC, "time");

        ProjectionOperation projectDate = Aggregation.project("date", "time", "num").andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(
                getSpecificDateUsernum,
                avgUsernum,
                filterAvgNum,
                sortOperation,
                projectDate
        );

        AggregationResults<Object> results = mongoTemplate.aggregate(
                aggregation,
                "usernum",
                Object.class
        );

        return new Result<List<Object>>(true, results.getMappedResults());
    }

    @Override
    public UpdateResult updateUsernumById(int id, Usernum updated_usernum) {
        Query query = new Query(Criteria.where("usernumId").is(id));
        Update update = new Update()
                .set("date",updated_usernum.getDate())
                .set("time",updated_usernum.getTime())
                .set("num",updated_usernum.getNum());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Usernum.class);
        return result;
    }

    @Override
    public int getMaxId() {
        Query query = new Query();
        query.limit(1).with(Sort.by(Sort.Direction.DESC, "usernumId"));
        return mongoTemplate.find(query, Usernum.class).get(0).getUsernumId();
    }

}
