package gift.repository.role;

import gift.dao.role.UserRoleDao;
import gift.entity.UserRole;

import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {
    private final UserRoleDao userRoleDao;

    public RoleRepositoryImpl(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        return userRoleDao.findByUserId(userId);
    }

    @Override
    public Boolean exists(Long userId, UserRole role) {
        if (userId == null || role == null) {
            throw new IllegalArgumentException("사용자 ID와 역할 이름은 null일 수 없습니다.");
        }
        return userRoleDao.exists(userId, role);
    }

    @Override
    public Boolean save(Long userId, UserRole role) {
        if (exists(userId, role)) {
            throw new IllegalArgumentException("이미 존재하는 사용자 역할입니다.");
        }
        return userRoleDao.save(userId, role) > 0;
    }

    @Override
    public Boolean delete(Long userId, UserRole role) {
        if (!exists(userId, role)) {
            throw new IllegalArgumentException("존재하지 않는 사용자 역할입니다.");
        }
        return userRoleDao.delete(userId, role) > 0;
    }

}
