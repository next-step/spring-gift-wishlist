package gift.member.application.usecase;

import gift.common.exception.ForbiddenException;
import gift.common.jwt.JwtTokenProvider;
import gift.common.security.PasswordEncoder;
import gift.member.application.port.in.LoginMemberUseCase;
import gift.member.application.port.in.RegisterMemberUseCase;
import gift.member.application.port.in.dto.AuthResponse;
import gift.member.application.port.in.dto.LoginRequest;
import gift.member.application.port.in.dto.RegisterRequest;
import gift.member.application.port.out.MemberPersistencePort;
import gift.member.domain.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberInteractor implements RegisterMemberUseCase, LoginMemberUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberInteractor(
            MemberPersistencePort memberPersistencePort,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        this.memberPersistencePort = memberPersistencePort;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (memberPersistencePort.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request.email(), encodedPassword);
        Member savedMember = memberPersistencePort.save(member);

        return createAuthResponse(savedMember.getId(), savedMember.getEmail(), savedMember.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Member member = memberPersistencePort.findByEmail(request.email())
                .orElseThrow(() -> new ForbiddenException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new ForbiddenException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return createAuthResponse(member.getId(), member.getEmail(), member.getRole());
    }

    private AuthResponse createAuthResponse(Long memberId, String email, gift.member.domain.model.Role role) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, email, role);
        return new AuthResponse(accessToken);
    }
} 