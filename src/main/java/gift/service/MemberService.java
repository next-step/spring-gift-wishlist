package gift.service;

import gift.dto.CreateMemberRequestDto;
import gift.dto.JWTResponseDto;

public interface MemberService {

    JWTResponseDto createMember(CreateMemberRequestDto requestDto);

    JWTResponseDto loginMember(CreateMemberRequestDto requestDto);
}
