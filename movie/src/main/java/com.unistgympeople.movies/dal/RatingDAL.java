package com.unistgympeople.movies.dal;

import java.util.List;

import com.unistgympeople.movies.model.Ratings;
public interface RatingDAL {

    List<Ratings> getAllRatings();

    Ratings getRatings(int MovieID);

    Ratings addNewRatings(Ratings ratings);
}
