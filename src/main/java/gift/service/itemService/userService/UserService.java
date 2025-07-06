package gift.service.itemService.userService;

import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;
import gift.dto.itemDto.userDto.UserUpdateDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRegisterDto dto);

    List<UserResponseDto> getUserList(String email);

    UserResponseDto finUserById(Long id);

    UserResponseDto updateUser(Long id, @Valid UserUpdateDto dto);
}
