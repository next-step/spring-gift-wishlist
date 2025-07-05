package gift.service.itemService.userService;

import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRegisterDto dto);
}
