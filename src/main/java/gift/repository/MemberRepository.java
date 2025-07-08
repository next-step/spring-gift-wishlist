package gift.repository;

import gift.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    void register(Member member);
    Optional<Member> findByEmail(String email);
}
