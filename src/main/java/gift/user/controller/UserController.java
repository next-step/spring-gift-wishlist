package gift.user.controller;

import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.service.UserService;
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

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerUser(
      @RequestBody RegisterRequestDto registerRequestDto) {

    return new ResponseEntity<RegisterResponseDto>(userService.registerUser(registerRequestDto),
        HttpStatus.CREATED);

  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> loginUser(
      @RequestBody LoginRequestDto loginRequestDto) {

    return new ResponseEntity<LoginResponseDto>(userService.loginUser(loginRequestDto),
        HttpStatus.OK);

  }


}
