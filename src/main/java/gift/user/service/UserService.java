package gift.user.service;

import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.entity.User;
import gift.user.repository.UserRepository;

public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        User user = userRepository.saveUser(registerRequestDto.email(),registerRequestDto.password());
        return RegisterResponseDto.from(user);
  }

  public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
    return new LoginResponseDto(loginRequestDto);
  }

}
