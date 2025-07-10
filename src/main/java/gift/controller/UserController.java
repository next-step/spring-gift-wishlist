package gift.controller;

import gift.dto.user.UserRequestDto;
import gift.dto.user.UserResponseDto;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        String return_token = userService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(return_token));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        String return_token = userService.login(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(return_token));
    }
}
