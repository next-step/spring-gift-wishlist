package gift.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.User;
import gift.dto.CreateUserRequest;
import gift.dto.CreateUserResponse;
import gift.exception.SignupException;
import gift.repository.AuthRepository;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        Long generatedId = authRepository.save(User.of(
            request.email(),
            passwordEncoder.encode(request.password()))
        );

        User user = authRepository.findById(generatedId)
            .orElseThrow(() -> new SignupException("사용자 생성에 실패했습니다."));

        return CreateUserResponse.from(user);
    }
}
