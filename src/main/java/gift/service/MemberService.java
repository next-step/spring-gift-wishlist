package gift.service;

import gift.dto.LoginRequest;
import gift.util.PasswordUtil;
import gift.dto.RegisterRequest;
import gift.dto.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public TokenResponse save(RegisterRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다: " + request.email());
        }

        String encryptedPassword = PasswordUtil.encode(request.password());

        Member member = new Member(request.email(), encryptedPassword);
        Member savedMember = memberRepository.save(member);

        String accessToken = Jwts.builder()
            .setSubject(savedMember.getId().toString())
            .claim("email", savedMember.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        return new TokenResponse(accessToken);
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email()).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.email()));
        ;

        if (!PasswordUtil.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        return new TokenResponse(accessToken);
    }
}
