package backup.movie.dal;

import java.util.List;

import backup.movie.model.User;

public interface UserDAL {

	List<User> getAllUsers();

	User getUserById(String userId);

	User addNewUser(User user);
}