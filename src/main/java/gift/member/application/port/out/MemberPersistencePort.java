package gift.member.application.port.out;

import gift.member.domain.model.Member;

import java.util.Optional;

public interface MemberPersistencePort {
    Member save(Member member);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
} 