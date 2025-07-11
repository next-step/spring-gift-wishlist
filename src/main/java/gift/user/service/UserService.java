package gift.user.service;

import gift.product.domain.Product;
import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import gift.user.dto.UserSaveRequestDto;
import gift.user.repository.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    @Transactional
    public User save(UserSaveRequestDto userSaveRequestDto) {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, userSaveRequestDto.getEmail(), userSaveRequestDto.getPassword());
        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String Email) {
        return userDao.findByEmail(Email);
    }

    @Transactional
    public User updateUser(UUID id, UserPatchRequestDto userPatchRequestDto) {
        userDao.findById(id);
        return userDao.update(id, userPatchRequestDto);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userDao.findById(id);
        userDao.delete(id);
    }
}
