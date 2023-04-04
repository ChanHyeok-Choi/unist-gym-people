package com.unistgympeople.movies.dal;

import java.util.List;

import com.unistgympeople.movies.model.User;

public interface UserDAL {

	List<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);
}