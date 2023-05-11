package com.unistgympeople.Calender.Service;

import com.unistgympeople.Calender.model.Calender;
import com.unistgympeople.Calender.repository.CalenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalenderServiceImpl implements CalenderService{
    @Autowired
    private CalenderRepository calenderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Calender calender){
        return calenderRepository.save(calender).getId();
    }

    @Override
    public List<Calender> getCalenderByMember(int memberid){
        return calenderRepository.findCalenderByMemberid(memberid);
    }
    @Override
    public List<Calender> getCalenderByMemberAndTime(int memberid, String time){
        return calenderRepository.findCalenderByMemberidAndTime(memberid, time);
    }

}
