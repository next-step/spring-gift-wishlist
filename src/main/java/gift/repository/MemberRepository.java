package gift.repository;

import gift.domain.Member;
import gift.dto.CreateMemberRequest;

import java.util.Optional;

public interface MemberRepository {
    Member save(CreateMemberRequest request);

    Optional<Member> findByEmail(String email);
}
