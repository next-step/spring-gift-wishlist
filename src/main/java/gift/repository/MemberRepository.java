package gift.repository;

import gift.entity.Member;

public interface MemberRepository {
    Member saveMember(Member member);
    boolean existsByEmail(String email);
}