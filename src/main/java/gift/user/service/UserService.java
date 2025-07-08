package gift.user.service;

import gift.security.PasswordEncoder;
import gift.user.JwtTokenProvider;
import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.domain.User;
import gift.exception.InvalidLoginException;
import gift.exception.UserNotFoundException;
import gift.user.dao.UserDao;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserDao userDao;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  public UserService(UserDao userDao, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
    String encryptedPassword = passwordEncoder.encrypt(registerRequestDto.email(), registerRequestDto.password());

    User user = userDao.saveUser(registerRequestDto.email(), encryptedPassword);

    String token = jwtTokenProvider.generateToken(user);

    return new RegisterResponseDto(token);
  }

  public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
    User user = userDao.findByEmail(loginRequestDto.email());

    if (user == null) {
      throw new UserNotFoundException();
    }

    if (!user.isEqualPassword(loginRequestDto.password(),passwordEncoder)) {
      throw new InvalidLoginException();
    }

    String token = jwtTokenProvider.generateToken(user);

    return new LoginResponseDto(token);
  }
}