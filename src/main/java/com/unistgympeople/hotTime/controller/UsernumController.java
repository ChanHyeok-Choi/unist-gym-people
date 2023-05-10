package com.unistgympeople.hotTime.controller;

import java.util.List;
import java.util.Optional;

import com.unistgympeople.hotTime.model.Usernum;
import com.unistgympeople.hotTime.service.UsernumService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unistgympeople.hotTime.exception.ObjectIdException;
import com.unistgympeople.hotTime.exception.ParameterErrorNumberException;
import com.unistgympeople.hotTime.exception.ParameterErrorStringException;
import com.unistgympeople.hotTime.model.Result;

@RestController
@RequestMapping("/usernums")
public class UsernumController {

    @Autowired
    private UsernumService usernumService;

    @ExceptionHandler(ObjectIdException.class)
    public ResponseEntity<String> handleObjectIdException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something wrong when saving the usernum");
    }
    @ExceptionHandler(ParameterErrorNumberException.class)
    public ResponseEntity<String> handleParameterErrorNumber() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usernum id does not exist!");
    }

    @ExceptionHandler(ParameterErrorStringException.class)
    public ResponseEntity<String> handleParameterErrorString() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Parameter is not a number!");
    }

    @PostMapping
    public ResponseEntity<Usernum> saveUsernum(@RequestBody Usernum usernum) {
        String new_id = usernumService.save(usernum);
        Optional<Usernum> new_usernum = usernumService.getUsernumById(new_id);
        if(new_usernum==null){
            throw new ObjectIdException("Something wrong when saving the usernum!");
        }
        return ResponseEntity.ok(new_usernum.get());
    }

    @GetMapping("/usernum/count")
    public int getUsernumCount(Usernum usernum) { return usernumService.getMaxId(); }

    @GetMapping("/all")
    public List<Usernum> getAllUsernum() { return usernumService.getUsernum(); }

    @GetMapping("/{id}")
    public ResponseEntity<Usernum> getUsernum(@PathVariable("id") String id) {
        if(id.length()==0) {
            throw new ParameterErrorStringException("Wrong id");
        }
        try {
            int usernum_id = Integer.parseInt(id);
            Optional<Usernum> result = usernumService.getUsernumById(usernum_id);
            if(result.isPresent()) {
                return ResponseEntity.ok(result.get());
            }
            throw new ParameterErrorNumberException("Id does not exist!");
        } catch (NumberFormatException e) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usernum> updateUsernum(@PathVariable("id") String id, @RequestBody Usernum usernum) {
        if(id.length()==0) {
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
        try {
            int usernum_id = Integer.parseInt(id);
            UpdateResult result = usernumService.updateUsernumById(usernum_id,usernum);
            if(result.getMatchedCount()==0) {
                throw new ParameterErrorNumberException("Id does not exits!");
            }
            if(!result.wasAcknowledged()) {
                throw new ObjectIdException("Something wrong when saving the usernum!");
            }
            return ResponseEntity.ok(usernumService.getUsernumById(id).get());
        } catch(NumberFormatException e){
            throw new ParameterErrorStringException("Parameter is not a number!");
        }
    }

    @GetMapping("/get/{date}")
    @ResponseBody
    public ResponseEntity<List<Object>> getUpperDate(@PathVariable("date") String date){
        try{
            Result<List<Object>> result = usernumService.getAvgUsernum(date);
            if(result.isOK()) {
                return ResponseEntity.ok(result.getResult());
            }
            throw new ParameterErrorNumberException("Parameter is not a date in a dataset!");
        } catch (NumberFormatException e) {
            throw new ParameterErrorStringException("Parameter is not a string!");
        }
    }
}
