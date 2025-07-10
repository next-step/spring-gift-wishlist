package gift.repository;

import gift.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member saveMember(Member member);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberById(Long id);
    List<Member> findAllMembers();
    void updateMember(Member member);
    void deleteMember(Long id);
}
