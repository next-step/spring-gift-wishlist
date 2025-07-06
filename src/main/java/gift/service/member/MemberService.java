package gift.service.member;

import gift.entity.Member;
import gift.entity.Token;

public interface MemberService {

  public Token register(Member member);

  public Token login(Member member);
}
