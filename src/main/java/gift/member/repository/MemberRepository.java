package gift.member.repository;

import gift.member.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    // findById와 findAll은 관리자용 API 구현 시 사용

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    Member save(Member member);

    Member updateProfile(Member member);

    void updatePassword(Member member);

    void remove(Long id);
}
