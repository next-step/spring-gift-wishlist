package gift.service;

import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.TokenDto;
import gift.common.exception.CreationFailException;
import gift.common.exception.RegisterFailException;
import gift.common.exception.SecurityException;
import gift.domain.member.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public MemberService(MemberRepository memberRepository,
                         JwtUtil jwt) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwt;
    }

    public TokenDto handleRegisterRequest(MemberRequestDto request) {
        register(request.email(), request.password());
        return login(request.email(), request.password());
    }

    public TokenDto handleLoginRequest(MemberRequestDto request) {
        return login(request.email(), request.password());
    }

    private Member register(String email, String plainPassword) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RegisterFailException("이미 가입된 email 입니다.", HttpStatus.FORBIDDEN);
        }
        String encryptedPassword = encoder.encode(plainPassword);
        Member instance = Member.createTemp(email, encryptedPassword);
        return memberRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Failed creating Member: " + email));
    }

    private TokenDto login(String email, String plainPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("존재하지 않는 email: " + email));
        if (!encoder.matches(plainPassword, member.getPassword())) {
            throw new SecurityException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtUtil.createToken(member.getEmail(), member.getRoleName());
        return new TokenDto(token);
    }
}
