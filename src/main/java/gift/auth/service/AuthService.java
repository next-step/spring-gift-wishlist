package gift.auth.service;

import gift.auth.JwtProvider;
import gift.auth.dto.UserSignupResponseDto;
import gift.user.domain.User;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.dto.UserSingupRequestDto;
import gift.user.repository.UserDao;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserDao userDao;
    private final JwtProvider jwtProvider;

    public AuthService(UserDao userDao, JwtProvider jwtProvider) {
        this.userDao = userDao;
        this.jwtProvider = jwtProvider;
    }

    public UserSignupResponseDto signUp(UserSingupRequestDto userSignupRequestDto) {
        UUID id = UUID.randomUUID();
        User user = new User(id, userSignupRequestDto.getEmail(), userSignupRequestDto.getPassword());

        return new UserSignupResponseDto(userDao.save(user), jwtProvider.createToken(user));
    }

    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDao.findByEmail(userLoginRequestDto.getEmail());

        if(!user.getPassword().equals(userLoginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
