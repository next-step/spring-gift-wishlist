package gift.service.userService;

import gift.Jwt.JwtUtil;
import gift.dto.userDto.UserLoginDto;
import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import gift.entity.User;
import gift.exception.UserDuplicatedException;
import gift.exception.UserNotFoundException;
import gift.exception.UserPasswordException;
import gift.repository.userRepository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public String registerUser(UserRegisterDto dto) {
        String email = dto.email();
        if (userRepository.findUserByEmail(email) != null) {
            throw new UserDuplicatedException();
        }
        User user = new User(null, dto.email(), dto.password());
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        return token;
    }

    @Override
    public String loginUser(UserLoginDto dto) {
        String targetEmail = dto.email();
        String targetPassword = dto.password();
        User findUser = userRepository.findUserByEmail(targetEmail);
        if (findUser == null) {
            throw new UserNotFoundException(targetEmail);
        }
        if (!findUser.password().equals(targetPassword)) {
            throw new UserPasswordException();
        }

        return jwtUtil.generateToken(findUser);

    }

    @Override
    public List<UserResponseDto> getUserList(String email) {
        List<User> users = getUsersByEmail(email);
        return addResponseDto(users);
    }

    private List<UserResponseDto> addResponseDto(List<User> users) {
        List<UserResponseDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponseDto(user.email(), user.password()));
        }
        return result;
    }

    private List<User> getUsersByEmail(String email) {
        if (email == null) {
            return userRepository.getAllUsers();
        } else {
            User findUser = userRepository.findUserByEmail(email);
            if (findUser == null) {
                throw new UserNotFoundException();
            } else {
                return List.of(findUser);
            }
        }
    }

    @Override
    public UserResponseDto finUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return new UserResponseDto(user.email(), user.password());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User findUser = userRepository.findUserById(id);
        String changeEmail = dto.email();
        String changePassword = dto.password();
        User updatedUser = userRepository.updateUser(findUser, changeEmail, changePassword);


        return new UserResponseDto(updatedUser.email(), updatedUser.password());
    }

    @Override
    public void deleteUserById(Long id) {
        User findUser = userRepository.findUserById(id);
        userRepository.deleteUser(findUser);
    }

}
