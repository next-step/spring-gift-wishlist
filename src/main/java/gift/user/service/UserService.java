package gift.user.service;

import gift.auth.PasswordUtil;
import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import gift.user.dto.UserSaveRequestDto;
import gift.user.repository.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    @Transactional
    public User save(UserSaveRequestDto userSaveRequestDto) throws Exception {

        UUID uuid = UUID.randomUUID();
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.encryptPassword(userSaveRequestDto.getPassword(), salt);
        User user = new User(uuid, userSaveRequestDto.getEmail(), hashedPassword, Base64.getEncoder().encodeToString(salt));
        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String Email) {
        return userDao.findByEmail(Email);
    }

    @Transactional
    public User updateUser(UUID id, UserPatchRequestDto userPatchRequestDto) throws Exception {
        if(userDao.findById(id).isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        if(userPatchRequestDto.getEmail() != null) {
            userDao.updateEmail(id, userPatchRequestDto.getEmail());
        }
        if(userPatchRequestDto.getPassword() != null) {
            byte[] salt = Base64.getDecoder().decode(userDao.findById(id).get().getSalt());
            String hashedPassword = PasswordUtil.encryptPassword(userPatchRequestDto.getPassword(), salt);
            userDao.updatePassword(id, hashedPassword);
        }
        return userDao.findById(id).get();
    }

    @Transactional
    public void deleteUser(UUID id) {
        if(userDao.findById(id).isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        userDao.delete(id);
    }
}
