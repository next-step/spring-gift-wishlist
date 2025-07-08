package gift.user.service;

import gift.security.PasswordEncoder;
import gift.user.JwtTokenProvider;
import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.entity.User;
import gift.exception.InvalidLoginException;
import gift.exception.UserNotFoundException;
import gift.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
    String encryptedPassword = passwordEncoder.encrypt(registerRequestDto.email(), registerRequestDto.password());

    User user = userRepository.saveUser(registerRequestDto.email(), encryptedPassword);

    String token = jwtTokenProvider.generateToken(user);

    return new RegisterResponseDto(token);
  }

  public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
    User user = userRepository.findByEmail(loginRequestDto.email());

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