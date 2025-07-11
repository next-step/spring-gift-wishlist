package gift.service;

import gift.dto.LoginRequest;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberNotFoundException;
import gift.util.PasswordUtil;
import gift.dto.RegisterRequest;
import gift.dto.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public TokenResponse save(RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        String encryptedPassword = PasswordUtil.encode(request.getPassword());

        Member member = new Member(request.getEmail(), encryptedPassword);
        Member savedMember = memberRepository.save(member);

        String accessToken = generateAccessToken(savedMember);

        return new TokenResponse(accessToken);
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() ->
            new MemberNotFoundException(request.getEmail()));
        ;

        if (!PasswordUtil.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = generateAccessToken(member);

        return new TokenResponse(accessToken);
    }

    public String generateAccessToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
