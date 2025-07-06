package gift.repository;

import gift.entity.Member;

public interface MemberRepository {
    void registerMember(String email, String password);

    boolean existsByEmail(String email);

    Member findMemberByEmailOrElseThrow(String email);
}
