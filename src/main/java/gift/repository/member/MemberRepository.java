package gift.repository.member;

import gift.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

  List<Member> findAllMembers();

  Optional<Member> findByEmail(String email);

  Optional<Member> findById(Long id);

  Member createMember(Member member);

  Optional<Member> updateMember(Long id, Member member);

  int deleteMember(Long id);
}

