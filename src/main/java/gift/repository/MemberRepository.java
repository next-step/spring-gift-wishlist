package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    void saveMember(Member member);
    Optional<Member> findByEmail(String email);
}
