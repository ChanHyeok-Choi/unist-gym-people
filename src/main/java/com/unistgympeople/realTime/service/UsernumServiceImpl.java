package com.unistgympeople.realTime.service;

import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UsernumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsernumServiceImpl implements UsernumService{

    @Autowired
    private UsernumRepository usernumRepository;
    @Autowired
    private MongoTemplate mongoTemplate2;

    public void setUsernumRepository(UsernumRepository usernumRepository) {
        this.usernumRepository = usernumRepository;
    }

    public void setMongoTemplate2(MongoTemplate mongoTemplate) {
        this.mongoTemplate2 = mongoTemplate;
    }

    @Override
    public String save(Usernum usernum,int number){
        LocalDate date = LocalDate.now();
        String strdate = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
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
        Query query = new Query(Criteria.where("date").is(date));
        List<Usernum> dates = mongoTemplate2.find(query,Usernum.class);

        long avg_num = 0;
        for (Usernum d : dates) {
            avg_num += d.getUserNumber();
        }
        avg_num /= dates.size();

        List<Usernum> results = new ArrayList<>();
        for (Usernum d : dates) {
            if (d.getUserNumber() > avg_num) {
                results.add(d);
            }
        }
        return results;
    }
}
