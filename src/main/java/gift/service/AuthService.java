package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;

public interface AuthService {

    TokenResponseDto signUp(MemberRequestDto requestDto);
    TokenResponseDto login(MemberRequestDto requestDto);
}
