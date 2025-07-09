package gift.service;

import gift.dto.LoginResponseDto;
import gift.dto.MemberRequestDto;

public interface AuthService {
    LoginResponseDto saveMember(MemberRequestDto dto);
    LoginResponseDto loginMember(MemberRequestDto dto);
}