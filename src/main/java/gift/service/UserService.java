package gift.service;

import gift.dto.UserRequestDto;
import gift.entity.User;
import gift.repository.H2UserRepository;
import gift.security.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final H2UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserService(H2UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public String register(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            return null;
        }
        User user = userRepository.save(userRequestDto.toEntity());
        return jwtProvider.generateToken(user);
    }
}
