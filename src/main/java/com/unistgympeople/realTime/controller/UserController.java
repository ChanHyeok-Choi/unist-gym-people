package com.unistgympeople.realTime.controller;

import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newEmployee(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/users/{id}")
    User replaceEmployee(@RequestBody User newUser, @PathVariable String id) {

        return userRepository.findById(id)
                .map(User -> {
                    User.setUserId(newUser.getUserId());
                    User.setTimeStamp(newUser.getTimeStamp());
                    return userRepository.save(User);
                })
                .orElseGet(() -> {
                    newUser.setUserId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteEmployee(@PathVariable String id) {
        userRepository.deleteById(id);
    }
}
