package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(LoginRequestDto loginRequestDto);

}
