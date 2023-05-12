package com.unistgympeople.Calender.Service;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.Calender.model.Exercise;
import com.unistgympeople.Calender.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService{
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Exercise exercise){
        return exerciseRepository.save(exercise).getId();
    }
    @Override
    public List<Exercise> getExercise(){
        return exerciseRepository.findAll();
    }
    @Override
    public List<Exercise> getExerciseByExercisetype(String exercisetype){
        Query query = new Query(Criteria.where("exercisetype").is(exercisetype));
        List<Exercise> result = mongoTemplate.find(query, Exercise.class);
        return result;

    }
    @Override
    public UpdateResult updateExerciseByExercisetype(String exercise_type, Exercise updated_exercise){
        Query query = new Query(Criteria.where("exercisetype").is(exercise_type));
        Update update = new Update()
                .set("percalorie", updated_exercise.getpercalorie());
        UpdateResult result = mongoTemplate.updateFirst(query,update,Exercise.class);
        return result;
    }


}