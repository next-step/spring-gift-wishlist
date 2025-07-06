package gift.repository;

import gift.entity.Member;

public interface MemberRepositoryInterface {

    Member findByEmail(String email);

    void save(Member member);
}
