package gift.repository.member;

import gift.entity.Member;
import java.util.Optional;

public interface MemberRepository {

  Optional<Member> findByEmail(String email);

  Member createMember(Member member);

}
