package com.unistgympeople.realTime.controller;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.lang.Object.*;

import com.sun.jdi.IntegerValue;
import com.unistgympeople.realTime.exception.ObjectIdException;
import com.unistgympeople.realTime.exception.ParameterErrorNumberException;
import com.unistgympeople.realTime.exception.ParameterErrorStringException;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.repository.UsernumRepository;
import com.unistgympeople.realTime.service.UserService;
import com.unistgympeople.realTime.service.UsernumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.UpdateResult;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsernumService usernumService;

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        String new_id = userService.save(user);
        Optional<User> new_user = userService.getUserById(new_id);
        if(new_user == null) {
            throw new ObjectIdException("Something wrong when saving the user!");
        }
        return ResponseEntity.ok(new_user.get());
    }

    @GetMapping("/userCount")
    public ResponseEntity<Long> getUserCount() {
        long userCount = userService.getUserCount();
        int userIntCount = Long.valueOf(userCount).intValue();
        Usernum usernum = new Usernum();
        String new_userid = usernumService.save(usernum,userIntCount);
        Optional<Usernum> new_usernum = usernumService.getUsernumById(new_userid);
        if(new_usernum == null) {
            throw new ObjectIdException("Something wrong when saving the user!");
        }
        return ResponseEntity.ok(userCount);
    }

    @GetMapping("/showCount")
    public List<Usernum> getAllUsernum() {
        List<Usernum> usernums = usernumService.getAllUsernum();
        return usernums;
    }

    @GetMapping("/showUser")
    public List<User> getAllUser() {
        List<User> users = userService.getAllUser();
        return users;
    }

    @GetMapping("/hotdate/{date}")
    public List<Usernum> getHotdate(@PathVariable("date") String date){
        List<Usernum> usernums = usernumService.getUsernumByDate(date);
        return usernums;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        if(id.length() == 0) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
        try {
            int user_id = Integer.parseInt(id);
            Optional<User> result = userService.getUserById(user_id);
            if(result.isPresent()) {
                return ResponseEntity.ok(result.get());
            }
            throw new ParameterErrorNumberException("User id does not exist!");
        } catch(NumberFormatException e) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        if(id.length() == 0) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
        try {
            int user_id = Integer.parseInt(id);
            UpdateResult result = userService.updateUserById(user_id, user);
            if(result.getMatchedCount() == 0) {
                throw new ParameterErrorNumberException("User id does not exist!");
            }
            if(!result.wasAcknowledged()) {
                throw new ObjectIdException("Something wrong when saving the user!");
            }
            return ResponseEntity.ok(userService.getUserById(user_id).get());
        } catch(NumberFormatException e) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
    }
}