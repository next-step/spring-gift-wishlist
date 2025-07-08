package gift.repository.member;

import gift.entity.member.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    int update(Member member);
    
    int deleteById(Long id);
}
