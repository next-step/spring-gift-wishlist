package gift.auth.service;

import gift.auth.JwtProvider;
import gift.auth.dto.UserSignupResponseDto;
import gift.common.exception.EmailAlreadyExistsException;
import gift.common.exception.InvalidPasswordException;
import gift.user.domain.User;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.dto.UserSingupRequestDto;
import gift.user.repository.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        if(userDao.findByEmail(userSignupRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        UUID id = UUID.randomUUID();
        User user = new User(id, userSignupRequestDto.getEmail(), userSignupRequestDto.getPassword());

        return new UserSignupResponseDto(userDao.save(user), jwtProvider.createToken(user));
    }

    public String login(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> optionalUser = userDao.findByEmail(userLoginRequestDto.getEmail());
        if(optionalUser.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        User user = optionalUser.get();
        if(!user.getPassword().equals(userLoginRequestDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
