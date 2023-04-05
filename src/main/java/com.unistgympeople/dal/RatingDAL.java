package com.unistgympeople.dal;

import java.util.List;

import com.unistgympeople.model.Ratings;
public interface RatingDAL {

    List<Ratings> getAllRatings();

    Ratings getRatings(int MovieID);

    Ratings addNewRatings(Ratings ratings);
}
