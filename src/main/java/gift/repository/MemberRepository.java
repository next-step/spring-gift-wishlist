package gift.repository;

import gift.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Member update(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    void delete(String email);

    List<Member> findAll();

}
