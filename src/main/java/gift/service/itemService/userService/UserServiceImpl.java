package gift.service.itemService.userService;

import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;
import gift.entity.User;
import gift.repository.itemRepository.userRepository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserResponseDto registerUser(UserRegisterDto dto) {
        User user = new User(null,dto.email(), dto.password());

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.email(),savedUser.password());
    }

    @Override
    public List<UserResponseDto> getUserList(String email) {
        List<User> users;
        List<UserResponseDto> result = new ArrayList<>();
        if (email == null) {
            users = userRepository.getAllUsers();
        }else
            users = userRepository.findUserByEmail(email);

        for (User user : users) {
            result.add(new UserResponseDto(user.email(),user.password()));
        }

        return result;
    }
}
