package gift.service;

import gift.common.exception.UserAlreadyExistsException;
import gift.domain.Role;
import gift.domain.User;
import gift.dto.user.CreateUserRequest;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(CreateUserRequest request) {
        Optional<User> getUser = userRepository.findByEmail(request.email());
        if (getUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(request.email(), request.password(), Role.USER);
        return userRepository.save(user);
    }

}
