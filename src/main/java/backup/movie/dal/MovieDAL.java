package backup.movie.dal;

import backup.movie.model.Movie;

import java.util.List;

public interface MovieDAL {

        List<Movie> getAllMovies();

        Movie getMovieById(String movieId);

        Movie addNewMovie(Movie movie);
}

