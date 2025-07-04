package gift.member.repository;

import gift.member.entity.Member;

public interface MemberRepository {

    void saveMember(Member member);

    void findMemberByEmail(String email);
}
