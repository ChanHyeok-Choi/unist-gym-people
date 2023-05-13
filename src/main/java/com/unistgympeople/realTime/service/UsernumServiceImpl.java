package com.unistgympeople.realTime.service;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UsernumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsernumServiceImpl implements UsernumService{

    @Autowired
    private UsernumRepository usernumRepository;
    @Autowired
    private MongoTemplate mongoTemplate2;

    @Override
    public String save(Usernum usernum,int number){
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        String strmonth = Integer.toString(month);
        String strdate = date.getYear() + "-" + strmonth + "-" + date.getDayOfMonth();
        usernum.setDate(strdate);

        LocalTime time = LocalTime.now();
        String strtime = time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
        usernum.setTime(strtime);

        usernum.setUserNumber(number);
        return usernumRepository.save(usernum).getId();
    }

    @Override
    public Optional<Usernum> getUsernumById(String id) {
        return usernumRepository.findById(id);
    }
    @Override
    public List<Usernum> getAllUsernum() {
        Query datequery = new Query();
        List<Usernum> dates = mongoTemplate2.find(datequery,Usernum.class);
        return dates;
    }
    @Override
    public List<Usernum> getUsernumByDate(String date) {
        MatchOperation getDateUsernum = Aggregation.match(
                Criteria.where("date").is(date)
        );

        GroupOperation avgUsernum = Aggregation.group("$date").avg("$userNum").as("avg_num");

        MatchOperation filterAvgNum = Aggregation.match(
                Criteria.where("userNum").gte("avg_num")
        );

        Aggregation aggregation = Aggregation.newAggregation(
                getDateUsernum,
                avgUsernum,
                filterAvgNum
        );

        List<Usernum> results = mongoTemplate2.aggregate(
                aggregation,
                "usernum",
                Usernum.class
        ).getMappedResults();

        return results;
    }
}
