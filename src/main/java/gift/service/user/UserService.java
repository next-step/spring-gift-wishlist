package gift.service.user;

import gift.entity.User;
import gift.entity.UserRole;
import gift.common.model.CustomPage;
import java.util.List;

public interface UserService {
    List<User> getAll();
    CustomPage<User> getBy(int page, int size);
    User getById(Long userId);
    User getByEmail(String email);
    User loadRoles(User user);
    User addRole(User user, UserRole role);
    User removeRole(User user, UserRole role);
    User create(User user);
    User update(User user);
    User patch(User user);
    void deleteById(Long userId);
}
