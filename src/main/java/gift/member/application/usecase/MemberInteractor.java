package gift.member.application.usecase;

import gift.common.jwt.JwtTokenProvider;
import gift.common.security.PasswordEncoder;
import gift.member.application.port.in.LoginMemberUseCase;
import gift.member.application.port.in.SignUpMemberUseCase;
import gift.member.application.port.in.dto.AuthResponse;
import gift.member.application.port.in.dto.LoginRequest;
import gift.member.application.port.in.dto.SignUpRequest;
import gift.member.application.port.out.MemberPersistencePort;
import gift.member.domain.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class MemberInteractor implements SignUpMemberUseCase, LoginMemberUseCase {

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
    public AuthResponse signUp(SignUpRequest request) {
        if (memberPersistencePort.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request.email(), encodedPassword);
        Member savedMember = memberPersistencePort.save(member);

        return createAuthResponse(savedMember.getId(), savedMember.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Member member = memberPersistencePort.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        return createAuthResponse(member.getId(), member.getEmail());
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        JwtTokenProvider.TokenValidationResult result = jwtTokenProvider.validateToken(refreshToken);
        
        if (!result.isValid()) {
            throw new IllegalArgumentException("Invalid refresh token: " + result.getErrorMessage());
        }
        
        String tokenType = jwtTokenProvider.getTokenTypeFromToken(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("Token is not a refresh token");
        }
        
        Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(memberId, email);
        long expiresInSeconds = jwtTokenProvider.getRemainingTimeInSeconds(newAccessToken);
        
        Instant accessTokenExpiry = Instant.now().plus(15, ChronoUnit.MINUTES);
        Instant refreshTokenExpiry = Instant.ofEpochMilli(result.getDecodedJWT().getExpiresAt().getTime());
        
        return AuthResponse.withRefreshToken(
                newAccessToken,
                refreshToken,
                expiresInSeconds,
                accessTokenExpiry,
                refreshTokenExpiry
        );
    }

    private AuthResponse createAuthResponse(Long memberId, String email) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, email);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, email);
        
        long expiresInSeconds = jwtTokenProvider.getRemainingTimeInSeconds(accessToken);
        Instant accessTokenExpiry = Instant.now().plus(15, ChronoUnit.MINUTES);
        Instant refreshTokenExpiry = Instant.now().plus(7, ChronoUnit.DAYS);
        
        return AuthResponse.withRefreshToken(
                accessToken, 
                refreshToken, 
                expiresInSeconds,
                accessTokenExpiry,
                refreshTokenExpiry
        );
    }
} 