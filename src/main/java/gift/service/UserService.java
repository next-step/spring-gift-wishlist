package gift.service;


import gift.auth.JwtTokenHandler;
import gift.dto.request.UserAuthRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;
import gift.exception.EmailDuplicationException;
import gift.exception.InvalidPasswordException;
import gift.exception.UserNotFoundException;
import gift.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    private final JwtTokenHandler jwtTokenHandler;


    public UserService(UserRepository userRepository, JwtTokenHandler jwtTokenHandler) {
        this.userRepository = userRepository;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    public User userWithEncodedPassword(UserAuthRequestDto userAuthRequestDto) {
        String encodedPassword = BCrypt.hashpw(userAuthRequestDto.password(), BCrypt.gensalt());
        return new User(userAuthRequestDto.email(), encodedPassword);
    }

    @Override
    public TokenResponseDto registerAndReturnToken(UserAuthRequestDto userAuthRequestDto) {

        if (userRepository.findUserByEmail(userAuthRequestDto.email()).isPresent()) {
            throw new EmailDuplicationException("중복된 이메일입니다");
        }

        User user = userWithEncodedPassword(userAuthRequestDto);
        userRepository.createUser(user);

        return new TokenResponseDto(jwtTokenHandler.createToken(user));
    }

    @Override
    public TokenResponseDto login(UserAuthRequestDto loginRequest) {
        User storedUser = userRepository.findUserByEmail(loginRequest.email())
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));

        if (!BCrypt.checkpw(loginRequest.password(), storedUser.password())) {
            throw new InvalidPasswordException("비밀번호가 다릅니다");
        }

        return new TokenResponseDto(jwtTokenHandler.createToken(storedUser));
    }
}
