package gift.repository.member;

import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    
    @Override
    public boolean alreadyRegitered(String email) {
        return false;
    }
    
    @Override
    public MemberResponseDto registerMember(Member newMember) {
        return null;
    }
}
