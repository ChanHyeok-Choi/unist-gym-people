package com.unistgympeople.Calender.controller;

import com.unistgympeople.Calender.Service.MemberService;
import com.unistgympeople.realTime.exception.ObjectIdException;
import com.unistgympeople.realTime.exception.ParameterErrorNumberException;
import com.unistgympeople.realTime.exception.ParameterErrorStringException;
import com.unistgympeople.Calender.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @ExceptionHandler(ObjectIdException.class)
    public ResponseEntity<String> handleObjectIdException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something wrong when saving the member");
    }

    @ExceptionHandler(ParameterErrorNumberException.class)
    public ResponseEntity<String> handleParameterErrorNumber() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Member id does not exist!");
    }

    @ExceptionHandler(ParameterErrorStringException.class)
    public ResponseEntity<String> handleParameterErrorString() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Parameter is not a number!");
    }
    @GetMapping("/{member}")
    @ResponseBody
    public Optional<Member> getMember(@PathVariable("member") int member){
        return memberService.getMemberById(member);
    }

    @GetMapping
    @ResponseBody
    public List<Member> getAllMember(){
        return memberService.getMember();
    }
    @PostMapping
    @ResponseBody
    public ResponseEntity<Member> saveMember(@RequestBody Member member){
        String new_id = memberService.save(member);
        Optional<Member> new_member = memberService.getMemberById(new_id);
        if(new_member == null){
            throw new ObjectIdException("Something wrong when saving the Member!");
        }
        return ResponseEntity.ok(new_member.get());
    }
}
