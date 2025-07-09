package gift.controller;

import gift.dto.UserDto;
import gift.service.UserService;
import org.springframework.http.HttpStatus;
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

    // 1. 회원 가입
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {

        UserDto response = userService.register(userDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto user) throws IllegalAccessException {

        // 요청으로 들어온 이메일, 비밀번호가 일치하면 JWT 토큰 발급
        String token = userService.login(user.getEmail(), user.getPassword());

        return ResponseEntity.ok(token);
    }

}
