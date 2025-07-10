package gift.user.service;

import gift.auth.JwtProvider;
import gift.user.domain.User;
import gift.user.dto.UserLoginRequestDto;
import gift.user.dto.UserSingupRequestDto;
import gift.user.repository.UserDao;
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

    public User signUp(UserSingupRequestDto userSignupRequestDto) {
        UUID id = UUID.randomUUID();
        User user = new User(id, userSignupRequestDto.getEmail(), userSignupRequestDto.getPassword());
        return userDao.save(user);
    }

    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDao.findByEmail(userLoginRequestDto.getEmail());

        if(!user.getPassword().equals(userLoginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
