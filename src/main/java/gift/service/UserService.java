package gift.service;


import gift.dto.request.LoginRequestDto;
import gift.dto.request.UserCreateRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.entity.User;
import gift.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User userWithEncodedPassword(UserCreateRequestDto userCreateRequestDto) {
        String encodedPassword = BCrypt.hashpw(userCreateRequestDto.password(), BCrypt.gensalt());
        return new User(userCreateRequestDto.email(), encodedPassword);
    }

    //나중에 환경변수로
    String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @Override
    public String createToken(User user) {
        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .claim("email", user.email())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) //30분
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    @Override
    public TokenResponseDto registerAndReturnToken(UserCreateRequestDto userCreateRequestDto) {

        User user = userWithEncodedPassword(userCreateRequestDto);
        userRepository.createUser(user);

        return new TokenResponseDto(createToken(user));
    }

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findUserByEmail(loginRequestDto.email())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다")); //나중에 exception 하나 만들어

        return new TokenResponseDto(createToken(user));

    }
}
