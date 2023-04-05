package com.unistgympeople.dal;

import java.util.List;

import com.unistgympeople.model.User;

public interface UserDAL {

	List<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);
}