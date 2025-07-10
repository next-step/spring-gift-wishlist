package gift.service;

import gift.dto.request.LoginRequest;
import gift.dto.request.SignUpRequest;
import gift.dto.response.TokenResponse;
import gift.entity.User;
import gift.exception.user.InValidPasswordException;
import gift.exception.user.NoUserException;
import gift.repository.AuthRepository;
import org.springframework.stereotype.Service;

import static gift.status.UserErrorStatus.*;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final TokenService tokenService;

    public AuthService(AuthRepository authRepository,  TokenService tokenService) {
        this.authRepository = authRepository;
        this.tokenService = tokenService;
    }

    public TokenResponse signup(SignUpRequest signUpRequest) {
        authRepository.save(signUpRequest.toEntity());
        return new TokenResponse(tokenService.generateToken(signUpRequest.email()));
    }

    public TokenResponse login(LoginRequest loginRequest) {
        User user = authRepository.findByEmail(
                loginRequest.email()).orElseThrow(() -> new NoUserException(NO_USER.getErrorMessage())
        );
        if(!user.isPasswordMatched(loginRequest.password())){
            throw new InValidPasswordException(INVALID_PASSWORD.getErrorMessage());
        }
        return new TokenResponse(tokenService.generateToken(loginRequest.email()));
    }
}
