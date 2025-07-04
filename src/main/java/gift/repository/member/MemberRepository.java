package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;

public interface MemberRepository {
    
    boolean alreadyRegistered(String email);
    
    MemberResponseDto registerMember(Member newMember);
}
