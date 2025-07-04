package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;

public interface MemberRepository {
    
    boolean existsByEmail(String email);
    
    Member registerMember(Member newMember);
    
    String findPassword(String email);
    
    Member findMember(String email);
}
