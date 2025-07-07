package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;

public interface MemberRepository {
    
    boolean existsByEmail(String email);
    
    Member registerMember(Member newMember);
    
    Member findMember(String email);
    
    Member findMemberById(Long userId);
}
