package gift.service.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;

public interface MemberService {
    
    MemberResponseDto registerMember(MemberRequestDto requestDto);
    
    MemberResponseDto loginMember(MemberRequestDto requestDto);
}
