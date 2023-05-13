package com.unistgympeople.realTime.service;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
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
        user.setUserType(user.getUserType());
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
                .set("timeStamp", updated_user.getTimeStamp())
                .set("userType", updated_user.getUserType());
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        return result;
    }

    @Override
    public int getMaxId() {
        Query query = new Query();
        query.limit(1).with(Sort.by(Sort.Direction.DESC, "userId"));
        return mongoTemplate.find(query, User.class).get(0).getUserId();
    }

    public int getUserCount() {
        Query enterquery = new Query(Criteria.where("userType").is("ENTER"));
        Query exitquery = new Query(Criteria.where("userType").is("EXIT"));
        int enternum = mongoTemplate.find(enterquery,User.class).size();
        int exitnum = mongoTemplate.find(exitquery,User.class).size();
        int num = enternum-exitnum;
        return num;
    }

    @Override
    public List<User> getAllUser() {
        Query datequery = new Query();
        List<User> dates = mongoTemplate.find(datequery,User.class);
        return dates;
    }
}
