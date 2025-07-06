package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepositoryInterface {

    Optional<Member> findByEmail(String email);

    void save(Member member);
}
