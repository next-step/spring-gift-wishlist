package gift.repository;

import gift.entity.User;
import java.util.Optional;

public interface UserRepository {

    void createUser(User user);

    void loginUser(User user);

    Optional<User> findUserByEmail(String email);


}
