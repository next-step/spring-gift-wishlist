package gift.service;

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

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        // TODO: 비밀번호 암호화 Best Practice 얼른 공부해서 적용하기!!
        Long generatedId = authRepository.save(User.of(request.email(), request.password()));

        User user = authRepository.findById(generatedId)
            .orElseThrow(() -> new SignupException("사용자 생성에 실패했습니다."));

        return CreateUserResponse.from(user);
    }
}
