package gift.repository;

import gift.dto.*;

public interface MemberRepository {
    MemberResponseDto createMember(MemberRequestDto memberRequestDto);

    PageResult<MemberResponseDto> findAllMembers(PageRequestDto pageRequestDto);

    MemberAuthDto findAuthByEmail(String email);

    MemberResponseDto findMemberById(Long id);

    MemberResponseDto findMemberByEmail(String email);

    MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto);

    void deleteMember(Long id);
}
