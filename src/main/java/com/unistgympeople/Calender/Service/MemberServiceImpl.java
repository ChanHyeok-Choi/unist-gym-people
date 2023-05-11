package com.unistgympeople.Calender.Service;

import com.unistgympeople.Calender.model.Member;
import com.unistgympeople.Calender.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Member member){
        member.setMemberid((getMaxId()+1));
        return memberRepository.save(member).getId();
    }
    @Override
    public List<Member> getMember(){return memberRepository.findAll();}
    @Override
    public Optional<Member> getMemberById(int id){
        Query query = new Query(Criteria.where("memberid").is(id));
        Member member = mongoTemplate.findOne(query, Member.class);
        if(member==null){
            return Optional.empty();
        }
        return Optional.of(member);
    }
    @Override
    public Optional<Member> getMemberById(String id){return memberRepository.findById(id);}
    @Override
    public int getMaxId(){
        Query query = new Query();
        query.limit(1).with(Sort.by(Sort.Direction.DESC,"memberid"));
        return mongoTemplate.find(query, Member.class).get(0).getMemberid();
    }
}
