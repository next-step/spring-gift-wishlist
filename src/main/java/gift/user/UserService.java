package gift.user;

import gift.auth.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;
    private final JwtProvider jwtProvider;

    public UserService(UserDao userDao, JwtProvider jwtProvider) {
        this.userDao = userDao;
        this.jwtProvider = jwtProvider;
    }

    public User signUp(UserRequestDto userRequestDto) {
        UUID id = UUID.randomUUID();
        User user = new User(id, userRequestDto.getEmail(), userRequestDto.getPassword());
        return userDao.save(user);
    }

    public String login(UserRequestDto userRequestDto) {
        User user = userDao.findByEmail(userRequestDto.getEmail());

        if(!user.getPassword().equals(userRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
