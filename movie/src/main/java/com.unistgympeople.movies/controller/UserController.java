package com.unistgympeople.movies.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unistgympeople.movies.repository.UserRepository;
import com.unistgympeople.movies.model.User;
import com.unistgympeople.movies.dal.UserDAL;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final UserDAL userDAL;
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository, UserDAL userDAL) {

		this.userRepository = userRepository;
		this.userDAL=userDAL;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return userRepository.findbyId(userId);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public User addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}
}
