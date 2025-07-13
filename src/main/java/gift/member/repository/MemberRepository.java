package gift.member.repository;

import gift.member.entity.Member;

import java.util.List;

public interface MemberRepository {
    void save(Member member);
    Member findByEmail(String email);
    List<Member> findAll();
    Member findById(Long id);
    void update(Member member);
    void delete(Long id);
}
