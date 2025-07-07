package gift.service;

import gift.dto.AuthLogInRequestDto;
import gift.dto.AuthResponseDto;
import gift.dto.AuthSignUpRequestDto;

public interface AuthService {
    AuthResponseDto registerMember(AuthSignUpRequestDto requestDto);
    AuthResponseDto findMemberToLogIn(AuthLogInRequestDto requestDto);
}
