package com.unistgympeople.movies.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unistgympeople.movies.repository.UserRepository;
import com.unistgympeople.movies.model.User;
import com.unistgympeople.movies.dal.UserDAL;

@RestController
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final UserDAL userDAL;
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository, UserDAL userDAL) {

		this.userRepository = userRepository;
		this.userDAL=userDAL;
	}

    @GetMapping("/users")
	public List<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}

	@GetMapping("/users/{userId}")
	public User getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return userRepository.findById(userId).orElseThrow(() -> new EmployeeNotFoundException(userId));
	}

	@PostMapping("/users/")
	public User addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}

	@PutMapping("/users/{userId}")
    public User replaceUser(@RequestBody User newUser, @PathVariable String userId) {
		LOG.info("replacing user.");
        return userRepository.findById(userId)
                .map(user -> {
                    user.setGender(newUser.getGender());
                    user.setAge(newUser.getAge());
                    user.setOccupation(newUser.getOccupation());
                    user.setZipcode(newUser.getZipcode());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setUserId(userId);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable String userId) {
        userRepository.deleteById(userId);
    }
}
