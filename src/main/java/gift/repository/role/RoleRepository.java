package gift.repository.role;

import gift.entity.UserRole;

import java.util.List;

public interface RoleRepository {
    List<UserRole> findByUserId(Long userId);
    Boolean exists(Long userId, UserRole role);
    Boolean save(Long userId, UserRole role);
    Boolean delete(Long userId, UserRole role);
}
