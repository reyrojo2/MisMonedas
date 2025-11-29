package interfaces;

import model.User;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    boolean save(User user);
    boolean existsByEmail(String email);
}
