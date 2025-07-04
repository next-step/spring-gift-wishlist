package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;

public interface MemberRepository {
    
    boolean alreadyRegistered(String email);
    
    MemberResponseDto registerMember(Member newMember);
    
    boolean wrongPassword(String email, String password);
    
    Member findMember(String email, String password);
    
    MemberResponseDto loginMember(Member member);
}
