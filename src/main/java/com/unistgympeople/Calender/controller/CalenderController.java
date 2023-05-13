package com.unistgympeople.Calender.controller;


import com.unistgympeople.Calender.Service.CalenderService;
import com.unistgympeople.Calender.model.Calender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Calender")
public class CalenderController {
    @Autowired
    private CalenderService calenderService;


    public CalenderController(){};

    public CalenderController(CalenderService calenderService){
        this.calenderService=calenderService;
    }

    @PostMapping
    public String save(@RequestBody Calender calender)
    {
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
        Integer result = calenderService.getCalorieByMemberAndTime(memberid, time);
        return result;
    }
}
