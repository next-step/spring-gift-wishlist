package gift.service;

import gift.DTO.LoginRequestDTO;
import gift.jwt.JwtTokenProvider;
import gift.model.User;
import gift.repository.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public String login(LoginRequestDTO login) {
        Optional<User> userOpt = userDao.findUserByUserid(login.getUserid());
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        return jwtTokenProvider.createToken(user.getUserid(),user.getPassword());
    }

    public User findByUserId(String userId) {
        Optional<User> userOpt = userDao.findUserByUserid(userId);
        User user = userOpt.orElseThrow(() -> new RuntimeException("<UNK>"));
        return user;
    }

    public void createUser(User user) {
        userDao.createUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    public Optional<User> findUserById(Long id) {
        return userDao.findUserById(id);
    }

    public void updateUser(Long id, User user) {
        userDao.updateUser(id, user);
    }
}