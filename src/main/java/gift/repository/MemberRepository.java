package gift.repository;

import gift.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member register(Member member);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);
}
