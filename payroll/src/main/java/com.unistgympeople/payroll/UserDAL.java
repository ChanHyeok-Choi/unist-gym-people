package com.unistgympeople.payroll;

import java.util.List;

public interface UserDAL {

	List<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);
}