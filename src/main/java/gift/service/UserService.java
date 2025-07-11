package gift.service;

import gift.dto.user.UserRequestDto;
import gift.model.User;
import gift.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일(사용자)입니다.");
        }
        User new_user = new User(userRequestDto.getEmail(),
                makeHashPwd(userRequestDto.getPassword()), userRequestDto.getName());
        userRepository.save(new_user);

        return makeToken(new_user);
    }

    public String login(UserRequestDto userRequestDto) {
        Optional<Long> get_id = userRepository.getIdFromEmail(userRequestDto.getEmail());
        Long user_id;
        if (get_id.isPresent()) {
            user_id = get_id.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이메일(사용자)입니다");
        }

        if (!userRepository.checkPassword(userRequestDto.getEmail(), userRequestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 틀립니다.");
        }

        User access_user = userRequestDto.toEntity();
        access_user.setId(user_id);

        return makeToken(access_user);
    }

    private String makeToken(User user) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private String makeHashPwd(String password) {
        return BCrypt.hashpw(
                password,
                BCrypt.gensalt());
    }

}
