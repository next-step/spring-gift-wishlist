package gift.service.itemService.userService;

import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRegisterDto dto);

    List<UserResponseDto> getUserList(String email);
}
