package gift.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.User;
import gift.dto.AdminUserResponse;
import gift.dto.SigninRequest;
import gift.dto.SigninResponse;
import gift.dto.SignupRequest;
import gift.dto.SignupResponse;
import gift.dto.UpdateUserRequest;
import gift.exception.SigninException;
import gift.exception.SignupException;
import gift.exception.UserDeleteException;
import gift.exception.UserNotFoundException;
import gift.exception.UserUpdateException;
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

    // TODO: 추후 브라우저 로컬스토리지나 쿠키에 토큰을 저장한다면, 토큰을 invalidate하는 signout 메서드 구현하기!

    @Transactional(readOnly = true)
    public List<AdminUserResponse> findAll() {
        return authRepository.findAll()
            .stream()
            .map(AdminUserResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public AdminUserResponse findById(Long id) {
        return authRepository.findById(id)
            .map(AdminUserResponse::from)
            .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    @Transactional
    public void update(Long id, UpdateUserRequest request) {
        checkUserExistence(id);

        User newUser = User.of(id, request.email(), null);
        int count = authRepository.update(newUser);
        if (count != 1) {
            throw new UserUpdateException("유저 이메일 수정을 실패했습니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        checkUserExistence(id);

        int count = authRepository.delete(id);
        if (count != 1) {
            throw new UserDeleteException("유저 삭제를 실패했습니다.");
        }
    }

    private void checkUserExistence(Long id) {
        if (!authRepository.existsById(id)) {
            throw new UserNotFoundException("해당 유저가 존재하지 않습니다.");
        }
    }
}
