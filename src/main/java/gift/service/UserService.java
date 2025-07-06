package gift.service;

import gift.common.exception.InvalidUserException;
import gift.common.exception.UserAlreadyExistsException;
import gift.common.exception.UserNotFoundException;
import gift.domain.Role;
import gift.domain.User;
import gift.dto.jwt.TokenResponse;
import gift.dto.user.ChangePasswordRequest;
import gift.dto.user.CreateUserRequest;
import gift.dto.user.LoginRequest;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User saveUser(CreateUserRequest request) {
        Optional<User> getUser = userRepository.findByEmail(request.email());
        if (getUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(request.email(), request.password(), Role.USER);
        return userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = getUserByEmail(request.email());
        if (user.isInvalidPassword(request.password())) {
            throw new InvalidUserException();
        }
        return TokenResponse.from(jwtTokenProvider.createToken(user));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getUserByEmail(request.email());
        if (user.isInvalidPassword(request.oldPassword())) {
            throw new InvalidUserException();
        }
        user.changePassword(request.newPassword());
        userRepository.update(user);
    }
}
