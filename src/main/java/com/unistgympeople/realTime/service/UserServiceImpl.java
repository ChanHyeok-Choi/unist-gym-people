package com.unistgympeople.realTime.service;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String save(User user){
        user.setUserId(getMaxId() + 1);
        user.setTimeStamp(user.getTimeStamp());
        return userRepository.save(user).getId();
}

    @Override
    public List<User> getUser(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(int id) {
        Query query = new Query(Criteria.where("userId").is(id));
        User user = mongoTemplate.findOne(query, User.class);
        if(user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public UpdateResult updateUserById(int id, User updated_user) {
        Query query = new Query(Criteria.where("userId").is(id));
        Update update = new Update()
                .set("timeStamp", updated_user.getTimeStamp());
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        return result;
    }

    @Override
    public int getMaxId() {
        Query query = new Query();
        query.limit(1).with(Sort.by(Sort.Direction.DESC, "userId"));
        return mongoTemplate.find(query, User.class).get(0).getUserId();
    }
}
