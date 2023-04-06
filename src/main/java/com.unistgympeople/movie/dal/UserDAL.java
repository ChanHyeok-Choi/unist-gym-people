package com.unistgympeople.movie.dal;

import java.util.List;

import com.unistgympeople.movie.model.User;

public interface UserDAL {

	List<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);
}