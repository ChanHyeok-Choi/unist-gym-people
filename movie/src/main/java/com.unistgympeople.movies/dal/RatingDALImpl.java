package com.unistgympeople.movies.dal;

import java.util.List;

import com.unistgympeople.movies.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movies.model.Ratings;
@Repository
public class RatingDALImpl implements RatingDAL{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Ratings> getAllRatings() {
        return mongoTemplate.findAll(Ratings.class);
    }

    @Override
    public Ratings getRatings(int MovieID) {
        Query query = new Query();
        query.addCriteria(Criteria.where("MovieID").is(MovieID));
        return mongoTemplate.findOne(query, Ratings.class);
    }

    @Override
    public Ratings addNewRatings(Ratings ratings) {
        mongoTemplate.save(ratings);
        return ratings;
    }
}
