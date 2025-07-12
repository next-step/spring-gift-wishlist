package gift.controller;


import gift.dto.request.LoginRequestDto;
import gift.dto.request.RegisterRequestDto;
import gift.dto.response.TokenResponseDto;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/members")
public class UserAuthenticationController {

    private final UserService userService;

    public UserAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> createUser(
        @RequestBody @Valid RegisterRequestDto registerRequestDto) {
        TokenResponseDto token = userService.registerAndReturnToken(registerRequestDto);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @RequestBody @Valid LoginRequestDto loginRequestDto
    ) {
        TokenResponseDto token = userService.login(loginRequestDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
