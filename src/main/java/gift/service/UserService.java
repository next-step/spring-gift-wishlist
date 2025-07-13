package gift.service;

import gift.dto.request.LoginRequestDto;
import gift.dto.request.RegisterRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;
import java.util.Optional;

public interface UserService {

    TokenResponseDto registerAndReturnToken(RegisterRequestDto registerRequestDto);

    TokenResponseDto login(LoginRequestDto loginRequest);

    User userWithEncodedPassword(RegisterRequestDto registerRequestDto);

    Optional<User> getUserByEmail(String email);

}
