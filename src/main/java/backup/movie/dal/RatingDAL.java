package backup.movie.dal;

import java.util.List;

import backup.movie.model.Ratings;

public interface RatingDAL {

    List<Ratings> getAllRatings();

    Ratings getRatings(int MovieID);

    Ratings addNewRatings(Ratings ratings);
}
