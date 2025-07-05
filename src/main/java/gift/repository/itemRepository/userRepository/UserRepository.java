package gift.repository.itemRepository.userRepository;

import gift.entity.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    List<User> getAllUsers();

    List<User> findUserByEmail(String email);

    User findUserById(Long id);
}
