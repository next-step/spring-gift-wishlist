package gift.service.itemService.userService;

import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;
import gift.entity.User;
import gift.repository.itemRepository.userRepository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserResponseDto registerUser(UserRegisterDto dto) {
        User user = new User(dto.email(), dto.password());

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.email(),savedUser.password());
    }
}
