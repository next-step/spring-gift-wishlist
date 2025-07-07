package gift.repository;

import gift.domain.Member;
import gift.dto.CreateMemberRequest;
import gift.dto.UpdateMemberRequest;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(CreateMemberRequest request);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    Member update(Long id, UpdateMemberRequest request);

    void delete(Long id);
}
