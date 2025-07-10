package gift.user.controller;

import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
      @Valid @RequestBody RegisterRequestDto registerRequestDto) {

    var registerResponse = userService.registerUser(registerRequestDto);

    return new ResponseEntity<RegisterResponseDto>(registerResponse, HttpStatus.CREATED);

  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> loginUser(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {

    return new ResponseEntity<LoginResponseDto>(userService.loginUser(loginRequestDto),
        HttpStatus.OK);

  }


}
