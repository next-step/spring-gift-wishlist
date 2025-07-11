package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final MemberRepository memberRepository;
    private String key = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public TokenServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<Member> isValidateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Optional<Member> find = memberRepository.findMemberByEmail(
                    claims.get("email", String.class));
            return find;

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String createAccessToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }
}
