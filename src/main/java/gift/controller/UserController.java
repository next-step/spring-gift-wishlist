package gift.controller;

import gift.dto.UserDto;
import gift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(UserDto userDto) {
        userService.register(userDto);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserDto user) {
    // 요청으로 들어온 이메일, 비밀번호가 일치하면 JWT 토큰 발급
        String token = userService.login(user.getEmail(), user.getPassword());

        return ResponseEntity.ok(token);
    }

}
