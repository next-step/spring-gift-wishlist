package gift.service;

import gift.dto.AuthTokenResponseDTO;
import gift.dto.MemberLoginRequestDTO;
import gift.dto.MemberRegisterRequestDTO;
import gift.entity.Member;
import gift.entity.Role;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthTokenResponseDTO register(MemberRegisterRequestDTO request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = new Member(0, request.email(), encodedPassword, Role.USER);
        Member savedMember = memberRepository.register(member);

        String token = jwtTokenService.generateJwtToken(savedMember);

        return new AuthTokenResponseDTO(token);
    }

    public AuthTokenResponseDTO login(MemberLoginRequestDTO request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenService.generateJwtToken(member);

        return new AuthTokenResponseDTO(token);
    }
}
