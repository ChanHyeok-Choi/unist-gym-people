package com.unistgympeople.payroll;

import java.util.List;

public interface RatingDAL {

    List<Ratings> getAllRatings();

    Ratings getRatings(int MovieID);

    Ratings addNewRatings(Ratings ratings);
}
