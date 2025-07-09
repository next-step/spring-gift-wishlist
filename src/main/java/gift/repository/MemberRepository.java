package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import java.util.Optional;

public interface MemberRepository {

    Member createMember(Member newMember);

    Optional<Member> findMemberByEmail(String email);

    void updateMemberPassword(Member member, String newPassword);

    void deleteMember(Member member);
}
