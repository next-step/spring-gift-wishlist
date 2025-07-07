package gift.service.member;

import gift.entity.Member;
import gift.entity.Token;
import java.util.List;

public interface MemberService {

  public Token register(Member member);

  public Token login(Member member);

  List<Member> findAllMember();

  Member findMemberById(Long id);

  Member createMember(Member member);

  Member updateMember(Long id, Member member);

  void deleteMember(Long id);
}
