package gift.repository;

import gift.domain.Member;

public interface MemberRepository {
    void registerMember(Member member);

    boolean existsByEmail(String email);

    Member findMemberByEmailOrElseThrow(String email);
}
