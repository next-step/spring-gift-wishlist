package gift.service.auth;

import gift.auth.JwtProvider;
import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.exception.forbidden.WrongPasswordException;
import gift.exception.notfound.NotRegisteredException;
import gift.repository.member.MemberRepository;

public class AuthServiceImpl implements AuthService {
    
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    
    public AuthServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        Member member = memberRepository.findMember(requestDto.email());
        
        if(!member.getPassword().equals(requestDto.password())) {
            throw new WrongPasswordException();
        }
        
        String accessToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(accessToken);
    }
}
