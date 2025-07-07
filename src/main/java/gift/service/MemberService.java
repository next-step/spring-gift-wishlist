package gift.service;

import gift.dto.LoginResponseDto;
import gift.dto.MemberRequestDto;

public interface MemberService {
    LoginResponseDto saveMember(MemberRequestDto dto);
    LoginResponseDto loginMember(MemberRequestDto dto);
}