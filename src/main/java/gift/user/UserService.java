package gift.user;

import gift.auth.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User signUp(UserRequestDto userRequestDto) {
        UUID id = UUID.randomUUID();
        User user = new User(id, userRequestDto.getEmail(), userRequestDto.getPassword());
        return userDao.save(user);
    }

    public String login(UserRequestDto userRequestDto, JwtProvider jwtProvider) {
        User user = userDao.findByEmail(userRequestDto.getEmail());

        return jwtProvider.createToken(user);
    }
}
