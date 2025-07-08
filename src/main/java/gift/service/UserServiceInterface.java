package gift.service;

import gift.dto.request.UserAuthRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;

public interface UserServiceInterface {

    TokenResponseDto registerAndReturnToken(UserAuthRequestDto userAuthRequestDto);

    TokenResponseDto login(UserAuthRequestDto loginRequest);

    User userWithEncodedPassword(UserAuthRequestDto userAuthRequestDto);

}
