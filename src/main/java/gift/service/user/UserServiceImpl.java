package gift.service.user;

import gift.entity.User;
import gift.entity.UserRole;
import gift.common.model.CustomPage;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    void validateUser(User user) {
        if (user == null || user.getId() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("사용자 정보가 유효하지 않습니다.");
        }
    }

    void validateRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("역할 정보가 유효하지 않습니다.");
        }
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public CustomPage<User> getBy(int page, int size) {
        return userRepository.findAll(page, size);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다. : " + userId));
    }

    @Override
    @Transactional
    public User loadRoles(User user) {
        validateUser(user);
        List<UserRole> roles = userRoleRepository.findByUserId(user.getId());
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("사용자의 역할을 찾을 수 없습니다. 사용자 ID: " + user.getId());
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    @Transactional
    public User addRole(User user, UserRole role) {
        validateUser(user);
        validateRole(role);
        this.userRoleRepository.save(user.getId(), role);
        return loadRoles(user);
    }

    @Override
    @Transactional
    public User removeRole(User user, UserRole role) {
        validateUser(user);
        validateRole(role);
        this.userRoleRepository.delete(user.getId(), role);
        return loadRoles(user);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("업데이트할 사용자 정보가 유효하지 않습니다.");
        }
        User existingUser = getById(user.getId());
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public User patch(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("패치할 사용자 정보가 유효하지 않습니다.");
        }
        User existingUser = getById(user.getId());
        if (user.getPassword() != null) {
            existingUser = userRepository.updateFieldById(user.getId(), "password", user.getPassword());
        }
        return existingUser;
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        getById(userId);
        if (!userRepository.deleteById(userId)) {
            throw new IllegalArgumentException("사용자를 삭제하는데 실패했습니다. 사용자 ID: " + userId);
        }
    }
}
