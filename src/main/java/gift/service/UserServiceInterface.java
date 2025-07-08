package gift.service;

import gift.dto.request.LoginRequestDto;
import gift.dto.request.UserCreateRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;

public interface UserServiceInterface {

    TokenResponseDto registerAndReturnToken(UserCreateRequestDto userCreateRequestDto);

    TokenResponseDto login(LoginRequestDto loginRequestDto);

    String createToken(User user);

    User userWithEncodedPassword(UserCreateRequestDto userCreateRequestDto);

}
