package gift.service.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    MemberRepository memberRepository;
    
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    
    @Override
    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
        return null;
    }
    
    @Override
    public MemberResponseDto loginMember(MemberRequestDto requestDto) {
        return null;
    }
}
