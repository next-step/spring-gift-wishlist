package gift.service.member;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;

public interface MemberService {
    
    LoginRequestDto registerMember(MemberRequestDto requestDto);
    
    LoginRequestDto findMemberToLogin(MemberRequestDto requestDto);
}
