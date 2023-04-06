package com.unistgympeople.movie.dal;

import java.util.List;

import com.unistgympeople.movie.model.Ratings;
public interface RatingDAL {

    List<Ratings> getAllRatings();

    Ratings getRatings(int MovieID);

    Ratings addNewRatings(Ratings ratings);
}
