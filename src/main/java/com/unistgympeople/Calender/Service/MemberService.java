package com.unistgympeople.Calender.Service;

import com.unistgympeople.Calender.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    String save(Member member);
    List<Member> getMember();

    public Optional<Member> getMemberById(int id);
    public Optional<Member> getMemberById(String id);
    public int getMaxId();
}
