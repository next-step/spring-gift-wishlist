package gift.service;

import gift.dto.AuthorizationRequest;
import gift.dto.AuthorizationResponse;
import gift.domain.Member;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public AuthorizationResponse register(AuthorizationRequest request) {
        Member member = Member.of(
                request.email(),
                request.password()
        );
        Member savedMember = memberRepository.save(member);

        String token = generateToken(savedMember.email());

        return AuthorizationResponse.of(token);
    }

    public AuthorizationResponse login(AuthorizationRequest request) {
        String email = request.email();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_EMAIL_NOT_FOUND));
        if(!member.isMatchPassword(request.password())){
            throw new BusinessException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        String token = generateToken(member.email());
        return AuthorizationResponse.of(token);
    }

    private String generateToken(String email){
        Claims claims = generateClaims(email);
        return JwtUtil.generateToken(claims);
    }

    private Claims generateClaims(String email){
        return Jwts.claims()
                .subject(email)
                .build();
    }
}
