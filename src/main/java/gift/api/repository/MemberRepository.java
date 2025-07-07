package gift.api.repository;

import gift.api.domain.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);
}
