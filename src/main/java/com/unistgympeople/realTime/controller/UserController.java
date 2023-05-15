package com.unistgympeople.realTime.controller;

import java.util.List;
import java.util.Optional;

import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.model.Usernum;
import com.unistgympeople.realTime.service.UserService;
import com.unistgympeople.realTime.service.UsernumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public void setUserController(UserService userService) {
        this.userService = userService;
    }

    public void setUsernumController(UsernumService usernumService) {
        this.usernumService = usernumService;
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        String new_id = userService.save(user);
        Optional<User> new_user = userService.getUserById(new_id);
        return ResponseEntity.ok(new_user.get());
    }

    @GetMapping("/userCount")
    public ResponseEntity<Long> getUserCount() {
        long userCount = userService.getUserCount();
        int userIntCount = Long.valueOf(userCount).intValue();
        Usernum usernum = new Usernum();
        String new_userid = usernumService.save(usernum,userIntCount);
        Optional<Usernum> new_usernum = usernumService.getUsernumById(new_userid);
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
        int user_id = Integer.parseInt(id);
        Optional<User> result = userService.getUserById(user_id);
        return ResponseEntity.ok(result.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        int user_id = Integer.parseInt(id);
        UpdateResult result = userService.updateUserById(user_id, user);
        return ResponseEntity.ok(userService.getUserById(user_id).get());
    }
}