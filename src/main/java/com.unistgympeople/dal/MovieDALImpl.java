package com.unistgympeople.movies.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.unistgympeople.movies.model.Movie;

@Repository
public class MovieDALImpl implements MovieDAL {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Movie> getAllMovies() {
        return mongoTemplate.findAll(Movie.class);
    }

    @Override
    public Movie getMovieById(String movieId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("movieId").is(movieId));
        return mongoTemplate.findOne(query, Movie.class);
    }

    @Override
    public Movie addNewMovie(Movie movie) {
        mongoTemplate.save(movie);
        // Now, movie object will contain the ID as well
        return movie;
    }
}
