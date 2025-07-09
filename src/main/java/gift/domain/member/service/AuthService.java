package gift.domain.member.service;

import gift.domain.member.Member;
import gift.domain.member.dto.LoginRequest;
import gift.domain.member.dto.SignInRequest;
import gift.global.exception.BadRequestException;
import gift.global.exception.LoginFailedException;
import gift.global.exception.TokenExpiredException;
import gift.global.exception.MemberNotFoundException;
import gift.domain.member.dto.TokenResponse;
import gift.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final String secretKey;

    public AuthService(MemberRepository memberRepository, @Value("${JWT_SECRET_KEY}") String secretKey) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
    }

    public TokenResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.email();
        String password = signInRequest.password();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()){
            throw new BadRequestException("AuthService : signIn() failed - Already registered member");
        }
        Member member = new Member(signInRequest.email(), signInRequest.password(), signInRequest.name(), signInRequest.role());
        memberRepository.save(member);
        Optional<Member> optionalMember1 = memberRepository.findByEmail(email);
        if (optionalMember1.isEmpty()){
            throw new MemberNotFoundException("AuthService : signIn() failed - Member not saved");
        }

        String accessToken = generateToken(email, password, optionalMember1.get());
        return new TokenResponse(accessToken);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()){
            throw new MemberNotFoundException("AuthService : login() failed - 404 Not Found Error");
        }
        Member user = optionalMember.get();
        if (!user.getPassword().equals(password)){
            throw new LoginFailedException("AuthService : login() failed - Wrong Password Error");
        }
        String accessToken = generateToken(email, password, user);
        return new TokenResponse(accessToken);
    }

    private String generateToken(String email, String password, Member member){
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String extractEmailFromAccessToken(String accessToken) throws TokenExpiredException {
        Claims claims = parseClaims(accessToken);
        if (!"access".equals(claims.get("tokenType", String.class))) {
            throw new BadRequestException("사용된 토큰이 엑세스 토큰이 아닙니다. 요청하신 로직에서는 엑세스 토큰으로만 처리가 가능합니다.");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new TokenExpiredException("액세스 토큰이 만료되었습니다. 리프레시 토큰으로 다시 액세스 토큰을 발급받으세요.");
        }
        return claims.getSubject();
    }

    public Claims parseClaims(String token) throws TokenExpiredException {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new TokenExpiredException(e.getMessage());
        }
    }
}
