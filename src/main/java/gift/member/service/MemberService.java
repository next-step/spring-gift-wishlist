package gift.member.service;

import gift.member.dto.*;

import java.util.List;

public interface MemberService {
    TokenResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto);
    TokenResponseDto login(MemberLoginRequestDto memberLoginRequestDto);
    List<MemberResponseDto> findAllMembers();
    MemberResponseDto findMemberById(Long id);
    void updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto);
    void deleteMember(Long id);
}
