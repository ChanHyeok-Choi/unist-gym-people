package com.unistgympeople.realTime.service;

import java.util.List;
import java.util.Optional;

import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.realTime.model.User;


public interface UserService {

    String save(User user);

    List<User> getUser();

    public Optional<User> getUserById(int id);

    public Optional<User> getUserById(String id);

    public UpdateResult updateUserById(int id, User updated_user);

    public int getMaxId();

    public int getUserCount();

    public List<User> getAllUser();
}
