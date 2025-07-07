package gift.service.userService;

import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRegisterDto dto);

    List<UserResponseDto> getUserList(String email);

    UserResponseDto finUserById(Long id);

    UserResponseDto updateUser(Long id, @Valid UserUpdateDto dto);

    void deleteUserById(Long id);
}
