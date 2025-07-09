package gift.service;

import gift.dto.request.LoginRequestDto;
import gift.dto.request.RegisterRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;

public interface UserServiceInterface {

    TokenResponseDto registerAndReturnToken(RegisterRequestDto registerRequestDto);

    TokenResponseDto login(LoginRequestDto loginRequest);

    User userWithEncodedPassword(RegisterRequestDto registerRequestDto);

}
