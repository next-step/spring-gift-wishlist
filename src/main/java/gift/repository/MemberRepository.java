package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    void registerMember(String email, String password);

    Optional<Member> findMemberByEmail(String email);
}
