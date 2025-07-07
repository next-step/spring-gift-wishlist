package gift.auth;

import gift.user.User;
import gift.user.UserDao;
import gift.user.UserRequestDto;
import gift.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserDao userDao;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, UserDao userDao, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userDao = userDao;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody UserRequestDto userRequestDto) {
        User user = userService.signUp(userRequestDto);

        return ResponseEntity
                .created(URI.create("/api/auth/" + user.getId()))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        User user = userDao.findByEmail(userRequestDto.getEmail());

        if(!user.getPassword().equals(userRequestDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 비밀번호입니다.");
        }
        String token = userService.login(userRequestDto, jwtProvider);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer"+token).body(token);
    }
}
