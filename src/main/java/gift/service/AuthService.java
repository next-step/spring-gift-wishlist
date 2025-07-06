package gift.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.User;
import gift.dto.SigninRequest;
import gift.dto.SigninResponse;
import gift.dto.SignupRequest;
import gift.dto.SignupResponse;
import gift.exception.SigninException;
import gift.exception.SignupException;
import gift.repository.AuthRepository;
import gift.util.TokenProvider;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(
        AuthRepository authRepository,
        PasswordEncoder passwordEncoder,
        TokenProvider tokenProvider
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (authRepository.existsByEmail(request.email())) {
            throw new SignupException("이미 가입된 이메일입니다.");
        }

        Long generatedId = authRepository.save(User.of(
            request.email(),
            passwordEncoder.encode(request.password()))
        );

        User user = authRepository.findById(generatedId)
            .orElseThrow(() -> new SignupException("사용자 생성에 실패했습니다."));

        return SignupResponse.from(user);
    }

    @Transactional(readOnly = true)
    public SigninResponse signin(SigninRequest request) {
        User user = authRepository.findByEmail(request.email())
            .orElseThrow(() -> new SigninException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new SigninException("비밀번호가 일치하지 않습니다.");
        }

        return new SigninResponse(tokenProvider.createToken(user.getId()));
    }
}
