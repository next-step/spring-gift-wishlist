package gift.repository;

import gift.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Member registerMember(Member member);
    Optional<Member> findMemberByEmail(String email);
}
