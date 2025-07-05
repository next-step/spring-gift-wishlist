package gift.member.service;

import gift.member.dto.RegisterRequestDto;
import gift.member.dto.RegisterResponseDto;

public interface MemberService {

    RegisterResponseDto registerMember(RegisterRequestDto registerRequestDto);

    void findMemberByEmail(RegisterRequestDto registerRequestDto);
}
