package gift.service;

import gift.dto.*;

public interface MemberService {

    MemberResponseDto register(MemberRequestDto dto);

    String login(LoginRequestDto loginRequestDto);

    PageResult<MemberResponseDto> findAllMembers(PageRequestDto pageRequestDto);

    MemberResponseDto findMemberById(Long id);

    MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto);

    void deleteMember(Long id);
}
