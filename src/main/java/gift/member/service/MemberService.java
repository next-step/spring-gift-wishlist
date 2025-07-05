package gift.member.service;

import gift.member.dto.RegisterRequestDto;
import gift.member.dto.TokenResponseDto;

public interface MemberService {

    TokenResponseDto registerMember(RegisterRequestDto registerRequestDto);

    void findMemberByEmail(RegisterRequestDto registerRequestDto);
}
