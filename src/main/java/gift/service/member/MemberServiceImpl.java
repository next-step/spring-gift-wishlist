package gift.service.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.conflict.AlreadyRegisteredException;
import gift.exception.forbidden.WrongPasswordException;
import gift.exception.notfound.NotRegisteredException;
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
        
        if(memberRepository.alreadyRegistered(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(0L, requestDto.email(), requestDto.password(), Role.USER);
        
        return memberRepository.registerMember(newMember);
    }
    
    @Override
    public MemberResponseDto loginMember(MemberRequestDto requestDto) {
        if(!memberRepository.alreadyRegistered(requestDto.email())) {
            throw new NotRegisteredException();
        }
        
        if(memberRepository.wrongPassword(requestDto.email(), requestDto.password())) {
            throw new WrongPasswordException();
        }
        
        Member member = memberRepository.findMember(requestDto.email(), requestDto.password());
        
        return memberRepository.loginMember(member);
    }
}
