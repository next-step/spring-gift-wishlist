package gift.service.userService;

import gift.dto.userDto.UserLoginDto;
import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    String registerUser(UserRegisterDto dto);

    List<UserResponseDto> getUserList(String email,boolean isAdmin);

    UserResponseDto finUserById(Long id);

    UserResponseDto updateUser(Long id, @Valid UserUpdateDto dto);

    void deleteUserById(Long id,boolean isAdmin);

    String loginUser(@Valid UserLoginDto dto);
}
