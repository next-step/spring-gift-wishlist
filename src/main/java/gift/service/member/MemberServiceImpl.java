package gift.service.member;

import gift.dto.api.member.LoginRequestDto;
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
    private final MemberRepository memberRepository;
    
    @Value(value = "${jwt.secret}")
    private String secretKey;
    
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Override
    public LoginRequestDto registerMember(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(0L, requestDto.email(), requestDto.password(), Role.USER);
        
        Member registeredMember = memberRepository.registerMember(newMember);
        
        return new LoginRequestDto(registeredMember);
    }
    
    @Override
    public LoginRequestDto findMemberToLogin(MemberRequestDto requestDto) {
        Member member = memberRepository.findMember(requestDto.email());
        return new LoginRequestDto(member);
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
