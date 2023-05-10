package com.unistgympeople.hotTime.service;

import java.util.List;
import java.util.Optional;

import com.unistgympeople.hotTime.model.Usernum;
import com.mongodb.client.result.UpdateResult;
import com.unistgympeople.hotTime.model.Result;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Update;

public interface UsernumService {

    String save(Usernum usernum);

    List<Usernum> getUsernum();

    public Optional<Usernum> getUsernumByDate(String date);

    public Optional<Usernum> getUsernumById(int id);

    public Optional<Usernum> getUsernumById(String id);

    Result<List<Object>> getAvgUsernum(String date);

    public UpdateResult updateUsernumById(int id, Usernum updated_usernum);

    public int getMaxId();

}
