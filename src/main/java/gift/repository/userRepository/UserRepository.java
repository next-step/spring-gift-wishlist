package gift.repository.userRepository;

import gift.entity.User;
import jakarta.validation.constraints.NotNull;


import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> getAllUsers();

    List<User> findUsersByEmail(String email);

    User findUserById(Long id);

    User updateUser(User findUser, String email, String password);

    void deleteUser(User findUser);


    User findUserByEmail(String email);
}
