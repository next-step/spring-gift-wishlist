package gift.repository;

import gift.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    void registerMember(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberById(Long id);
}
