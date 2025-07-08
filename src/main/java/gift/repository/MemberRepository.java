package gift.repository;

import gift.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member registerMember(Member member);
    Optional<Member> findMemberByEmail(String email);
    List<Member> findAllMembers();
    Member findMemberById(Long id);
    void updateMember(Member member);
    void deleteMember(Long id);
}
