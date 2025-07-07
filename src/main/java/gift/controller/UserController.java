package gift.controller;

import gift.dto.TokenResponseDto;
import gift.dto.UserRequestDto;
import gift.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.register(userRequestDto);
        return new ResponseEntity<>(new TokenResponseDto(token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.login(userRequestDto);
        return new ResponseEntity<>(new TokenResponseDto(token), HttpStatus.OK);
    }
}
