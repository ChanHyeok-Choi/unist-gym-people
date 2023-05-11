package com.unistgympeople.Calender.controller;


import com.unistgympeople.Calender.Service.CalenderService;
import com.unistgympeople.Calender.exception.ObjectIdException;
import com.unistgympeople.Calender.model.Calender;
import com.unistgympeople.Calender.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Calender")
public class CalenderController {
    @Autowired
    private CalenderService calenderService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping
    public String save(@RequestBody Calender calender)
    {
        String exercise_type = calender.getEvent();
        Query query = new Query();
        query.addCriteria(Criteria.where("exercisetype").is(exercise_type));
        Exercise exercise = mongoTemplate.findOne(query,Exercise.class);
        if(exercise == null)
        {throw new ObjectIdException("Exercise type not found!");}
        if(calender.getNum() <=0)
        {throw new ObjectIdException("Number must be positive");}
        return calenderService.save(calender);
    }

    @GetMapping("/{memberid}")
    @ResponseBody
    public List<Calender> getEvents(@PathVariable("memberid") int memberid){
        List<Calender> result = calenderService.getCalenderByMember(memberid);
        return result;
    }
    @GetMapping("/{memberid}/{time}")
    @ResponseBody
    public List<Calender> getEventsOnDate(@PathVariable("memberid") int memberid, @PathVariable("time") String time){
        List<Calender> result = calenderService.getCalenderByMemberAndTime(memberid, time);
        return result;
    }

    @GetMapping("/{memberid}/{time}/Calorie")
    @ResponseBody
    public Integer getCalorieOnDate(@PathVariable("memberid") int memberid, @PathVariable("time") String time){

        Query query = new Query();
        query.addCriteria(Criteria.where("memberid").is(memberid));
        query.addCriteria(Criteria.where("time").is(time));
        List<Calender> result = mongoTemplate.find(query, Calender.class);
        Integer total = 0;
        for(int i = 0; i < result.size(); i++)
        {
            Calender now = result.get(i);
            Query innerquery = new Query();
            innerquery.addCriteria(Criteria.where("exercisetype")
                    .is(now.getEvent()));
            Exercise exercise = mongoTemplate.findOne(innerquery,Exercise.class);
            if(exercise == null){
                throw new ObjectIdException("Exercise type not found!");
            }
            Integer count = now.getNum()*exercise.getpercalorie();
            total = total + count;
        }
        return total;
    }
}
