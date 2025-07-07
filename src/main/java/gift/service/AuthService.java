package gift.service;

import gift.dto.UserRequestDto;
import gift.dto.UserResponseDto;
import gift.entity.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    public AuthService(UserRepository userRepository) {this.userRepository = userRepository;}

    public UserResponseDto userSignUp(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto);
        return new UserResponseDto(userRepository.createUser(user));
    }

    public void userLogin(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto);
        userRepository.checkUser(user);
    }
}
