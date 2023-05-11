package com.unistgympeople.Calender.Service;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.Calender.model.Exercise;

import java.util.List;

public interface ExerciseService {
    public String save(Exercise exercise);
    public List<Exercise> getExercise();
    public List<Exercise> getExerciseByExercisetype(String exercise_type);
    public UpdateResult updateExerciseByExercisetype(String exercise_type, Exercise updated_exercise);

}
