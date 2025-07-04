package gift.service.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.conflict.AlreadyRegisteredException;
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
        
        if(memberRepository.alreadyRegitered(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(0L, requestDto.email(), requestDto.password(), Role.USER);
        
        return memberRepository.registerMember(newMember);
    }
    
    @Override
    public MemberResponseDto loginMember(MemberRequestDto requestDto) {
        return null;
    }
}
