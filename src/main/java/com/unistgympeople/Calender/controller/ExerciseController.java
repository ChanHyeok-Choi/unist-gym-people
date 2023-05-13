package com.unistgympeople.Calender.controller;

import com.unistgympeople.Calender.Service.ExerciseService;
import com.unistgympeople.Calender.model.Exercise;
import com.unistgympeople.realTime.exception.ObjectIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Exercise")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    public ExerciseController(){};
    public ExerciseController(ExerciseService exerciseService) {this.exerciseService=exerciseService;}

    @PostMapping
    public String save(@RequestBody Exercise exercise){
        return exerciseService.save(exercise);
    }
    @GetMapping
    @ResponseBody
    public List<Exercise> getAllExercise(){
        return exerciseService.getExercise();
    }
    @GetMapping("/{exercisetype}")
    @ResponseBody
    public List<Exercise> getEvents(@PathVariable("exercisetype") String exercisetype){
        List<Exercise> result = exerciseService.getExerciseByExercisetype(exercisetype);
        return result;
    }
}
