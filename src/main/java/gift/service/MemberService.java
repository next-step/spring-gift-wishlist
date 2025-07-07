package gift.service;

import gift.dto.MemberLogInRequestDto;
import gift.dto.MemberLogInResponseDto;

public interface MemberService {
    MemberLogInResponseDto registerMember(MemberLogInRequestDto requestDto);
    MemberLogInResponseDto findMemberToLogIn(MemberLogInRequestDto requestDto);
}
