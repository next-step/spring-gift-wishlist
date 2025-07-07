package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Member saveMember(Member member);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}