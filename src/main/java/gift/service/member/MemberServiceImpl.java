package gift.service.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.conflict.AlreadyRegisteredException;
import gift.exception.forbidden.WrongPasswordException;
import gift.exception.notfound.NotRegisteredException;
import gift.repository.member.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;
    
    @Value(value = "${jwt.secret}")
    private String secretKey;
    
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Override
    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
        if(memberRepository.existsByEmail(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(0L, requestDto.email(), requestDto.password(), Role.USER);
        
        Member registeredMember = memberRepository.registerMember(newMember);
        
        String accessToken = createToken(registeredMember);
        
        return new MemberResponseDto(accessToken);
    }
    
    @Override
    public MemberResponseDto loginMember(MemberRequestDto requestDto) {
        if(!memberRepository.existsByEmail(requestDto.email())) {
            throw new NotRegisteredException();
        }
        
        String checkPassword = memberRepository.findPassword(requestDto.email());
        
        if(!checkPassword.equals(requestDto.password())) {
            throw new WrongPasswordException();
        }
        
        Member member = memberRepository.findMember(requestDto.email());
        
        String accessToken = createToken(member);
        
        return new MemberResponseDto(accessToken);
    }
    
    private String createToken(Member member) {
        return Jwts.builder()
            .subject(Long.toString(member.getId()))
            .claim("email", member.getEmail())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }
}
