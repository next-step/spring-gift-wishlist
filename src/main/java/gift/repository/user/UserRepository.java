package gift.repository.user;


import gift.entity.User;
import gift.common.model.CustomPage;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    CustomPage<User> findAll(int page, int size);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    User save(User user);
    User updateFieldById(Long userId, String fieldName, Object value);
    Boolean deleteById(Long userId);
}
