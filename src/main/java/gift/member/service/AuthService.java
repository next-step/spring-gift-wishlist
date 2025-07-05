package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
