package gift.repository.userRepository;

import gift.entity.User;


import java.util.List;

public interface UserRepository {
    User save(User user);

    List<User> getAllUsers();

    List<User> findUserByEmail(String email);

    User findUserById(Long id);

    User updateUser(User findUser, String email, String password);

    void deleteUser(User findUser);
}
