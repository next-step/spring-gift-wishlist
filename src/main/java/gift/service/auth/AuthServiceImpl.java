package gift.service.auth;

import gift.auth.JwtProvider;
import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.common.UnauthorizedException;
import gift.exception.forbidden.WrongPermissionException;
import gift.exception.unauthorized.WrongHeaderException;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    
    public AuthServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    public MemberResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findMember(requestDto.email());
        
        if(!member.getPassword().equals(requestDto.password())) {
            throw new WrongIdOrPasswordException();
        }
        
        String accessToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(accessToken);
    }
    
    @Override
    public void checkPermissonForAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new WrongHeaderException();
        }
        
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token);
        
        String email = claims.get("email", String.class);
        Member member = memberRepository.findMember(email);
        
        Role role = Role.valueOf(claims.get("role", String.class));
        
        if(!role.equals(Role.ADMIN) || !role.equals(member.getRole())) {
            throw new WrongPermissionException();
        }
    }
    
    @Override
    public Long checkPermissonForUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new WrongHeaderException();
        }
        
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token);
        
        String email = claims.get("email", String.class);
        Member member = memberRepository.findMember(email);
        
        return member.getId();
    }
}
