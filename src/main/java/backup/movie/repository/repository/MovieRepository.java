package backup.movie.repository.repository;
import backup.movie.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>{
    // Movie findOne(String movieId);
}
