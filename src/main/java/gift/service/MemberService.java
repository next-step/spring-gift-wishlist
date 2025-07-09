package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRegisterRequestDto;
import gift.dto.MemberUpdateRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;

import java.util.List;

public interface MemberService {
    TokenResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto);
    String login(MemberLoginRequestDto memberLoginRequestDto);
    List<Member> findAllMembers();
    Member findMemberById(Long id);
    void updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto);
    void deleteMember(Long id);
}
