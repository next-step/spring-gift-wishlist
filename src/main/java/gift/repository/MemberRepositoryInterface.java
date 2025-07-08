package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepositoryInterface {

    Optional<Member> findByEmail(String email);

    void save(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
