package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.dto.RegisterRequestDto;
import gift.member.dto.RegisterResponseDto;

public interface MemberService {

    RegisterResponseDto registerMember(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
