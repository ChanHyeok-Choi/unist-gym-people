package com.unistgympeople.realTime.service;

import com.unistgympeople.realTime.model.Usernum;

import java.util.List;
import java.util.Optional;


public interface UsernumService {

    String save(Usernum usernum, int number);
    Optional<Usernum> getUsernumById(String id);
    List<Usernum> getAllUsernum();
    List<Usernum> getUsernumByDate(String date);
}
