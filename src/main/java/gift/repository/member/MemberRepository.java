// src/main/java/gift/dao/MemberDao.java
package gift.repository.member;

import gift.entity.member.Member;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
