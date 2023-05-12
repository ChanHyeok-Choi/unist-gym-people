package com.unistgympeople.Calender.Service;

import com.unistgympeople.Calender.exception.ObjectIdException;
import com.unistgympeople.Calender.model.Calender;
import com.unistgympeople.Calender.model.Exercise;
import com.unistgympeople.Calender.repository.CalenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalenderServiceImpl implements CalenderService {
    @Autowired
    private CalenderRepository calenderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String save(Calender calender) {
        {
            String exercise_type = calender.getEvent();
            Query query = new Query();
            query.addCriteria(Criteria.where("exercisetype").is(exercise_type));
            Exercise exercise = mongoTemplate.findOne(query, Exercise.class);
            if (exercise == null) {
                throw new ObjectIdException("Exercise type not found!");
            }
            if (calender.getNum() <= 0) {
                throw new ObjectIdException("Number must be positive");
            }
            return calenderRepository.save(calender).getId();
        }
    }

    @Override
    public List<Calender> getCalenderByMember(int memberid) {
        return calenderRepository.findCalenderByMemberid(memberid);
    }

    @Override
    public List<Calender> getCalenderByMemberAndTime(int memberid, String time) {
        return calenderRepository.findCalenderByMemberidAndTime(memberid, time);
    }

    @Override
    public Integer getCalorieByMemberAndTime(int memberid, String time)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").

        is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        List<Calender> result = mongoTemplate.find(query, Calender.class);
        Integer total = 0;
        for(int i = 0; i<result.size();i++)
        {
            Calender now = result.get(i);
            Query innerquery = new Query();
            innerquery.addCriteria(Criteria.where("exercisetype").is(now.getEvent()));
            Exercise exercise = mongoTemplate.findOne(innerquery, Exercise.class);
            if (exercise == null) {
                throw new ObjectIdException("Exercise type not found!");
            }
        Integer count = now.getNum() * exercise.getpercalorie();
        total = total + count;
        }
        return total;
    }

}
