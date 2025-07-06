package gift.user.service;

import gift.user.JwtTokenProvider;
import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.entity.User;
import gift.user.repository.UserRepository;

public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
    this.userRepository = userRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        User user = userRepository.saveUser(registerRequestDto.email(),registerRequestDto.password());

        String token = jwtTokenProvider.generateToken(user);

        return RegisterResponseDto.from(user);
  }

  public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
    return new LoginResponseDto(loginRequestDto);
  }
