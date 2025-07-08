package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRegisterRequestDto;
import gift.entity.Member;

import java.util.List;

public interface MemberService {
    String register(MemberRegisterRequestDto memberRegisterRequestDto);
    String login(MemberLoginRequestDto memberLoginRequestDto);
    List<Member> findAllMembers();
    Member findMemberById(Long id);
    void updateMember(Long id, MemberRegisterRequestDto memberRegisterRequestDto);
    void deleteMember(Long id);
}
