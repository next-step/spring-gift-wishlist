package gift.repository;

import gift.domain.Member;

public interface MemberRepository {
    void registerMember(String email, String password);

    boolean existsByEmail(String email);

    Member findMemberByEmailOrElseThrow(String email);
}
