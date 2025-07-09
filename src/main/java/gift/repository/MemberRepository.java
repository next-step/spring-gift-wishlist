package gift.repository;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void saveMember(Member member);
    Optional<Member> findByEmail(String email);
    List<MemberResponseDto> getAllMembers();
    Optional<Member> getMemberById(Long id);
    void updateMember(Long id, MemberRequestDto memberRequestDto);
    void deleteMember(Long id);
}
