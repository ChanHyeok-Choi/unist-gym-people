package com.unistgympeople.Calender.Service;

import com.unistgympeople.Calender.model.Calender;

import java.util.List;

public interface CalenderService {
    String save(Calender calender);

    public List<Calender> getCalenderByMember(int memberid);

    public List<Calender> getCalenderByMemberAndTime(int memberid, String time);

    public Integer getCalorieByMemberAndTime(int memberid, String time);
}
