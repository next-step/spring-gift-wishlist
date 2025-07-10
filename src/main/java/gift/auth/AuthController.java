package gift.auth;

import gift.user.domain.User;
import gift.user.dto.UserLoginRequestDto;
import gift.user.service.UserService;
import gift.user.dto.UserSingupRequestDto;
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
    public ResponseEntity<?> singup(@RequestBody UserSingupRequestDto userSignupRequestDto) {
        User user = userService.signUp(userSignupRequestDto);

        return ResponseEntity
                .created(URI.create("/api/auth/" + user.getId()))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            String token = userService.login(userLoginRequestDto);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer"+token).body(token);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
