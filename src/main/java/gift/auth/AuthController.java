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

    public AuthController(UserService userService) {
        this.userService = userService;
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
        try {
            String token = userService.login(userRequestDto);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer"+token).body(token);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
