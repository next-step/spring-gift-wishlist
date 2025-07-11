package gift.auth.service;

import gift.auth.JwtProvider;
import gift.auth.PasswordUtil;
import gift.auth.dto.UserSignupResponseDto;
import gift.common.exception.EmailAlreadyExistsException;
import gift.common.exception.InvalidPasswordException;
import gift.user.domain.User;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.dto.UserSingupRequestDto;
import gift.user.repository.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Base64;
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

    public UserSignupResponseDto signUp(UserSingupRequestDto userSignupRequestDto) throws Exception {
        if(userDao.findByEmail(userSignupRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        UUID id = UUID.randomUUID();
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.encryptPassword(userSignupRequestDto.getPassword(), salt);

        User user = new User(id, userSignupRequestDto.getEmail(), hashedPassword, Base64.getEncoder().encodeToString(salt));

        return new UserSignupResponseDto(userDao.save(user), jwtProvider.createToken(user));
    }

    public String login(UserLoginRequestDto userLoginRequestDto) throws Exception {
        Optional<User> optionalUser = userDao.findByEmail(userLoginRequestDto.getEmail());
        if(optionalUser.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        User user = optionalUser.get();
        if(!user.isEqualToPassword(userLoginRequestDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
