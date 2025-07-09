package gift.domain.member.service;

import gift.domain.member.Member;
import gift.domain.member.dto.LoginRequest;
import gift.domain.member.dto.SignInRequest;
import gift.domain.member.jwt.JwtProvider;
import gift.global.exception.BadRequestException;
import gift.global.exception.LoginFailedException;
import gift.global.exception.MemberNotFoundException;
import gift.domain.member.dto.TokenResponse;
import gift.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.email();
        if (memberRepository.existsByEmail(email)){
            throw new BadRequestException("AuthService : signIn() failed - Already registered member");
        }
        Member member = new Member(email, signInRequest.password(), signInRequest.name(), signInRequest.role());
        memberRepository.save(member);
        if (memberRepository.existsByEmail(email)){
            throw new MemberNotFoundException("AuthService : signIn() failed - Member not saved");
        }
        String accessToken = jwtProvider.generateToken(member);
        return new TokenResponse(accessToken);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        if (!memberRepository.existsByEmail(email)){
            throw new MemberNotFoundException("AuthService : login() failed - 404 Not Found Error");
        }
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("AuthService : login() failed - Member not found"));
        if (!member.verifyPassword(password)){
            throw new LoginFailedException("AuthService : login() failed - Wrong Password Error");
        }
        String accessToken = jwtProvider.generateToken(member);
        return new TokenResponse(accessToken);
    }
}
