package gift.user.controller;

import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.service.UserService;
import gift.validation.PasswordValidator;
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
  private final PasswordValidator passwordValidator;

  public UserController(UserService userService, PasswordValidator passwordValidator) {
    this.userService = userService;
    this.passwordValidator = passwordValidator;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerUser(
      @Valid @RequestBody RegisterRequestDto registerRequestDto,
      BindingResult bindingResult) {

    // HACK : Controller에서 Getter을 사용하고 있다.
    passwordValidator.validate(registerRequestDto.password(), bindingResult);

    return new ResponseEntity<RegisterResponseDto>(userService.registerUser(registerRequestDto),
        HttpStatus.CREATED);

  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> loginUser(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {

    return new ResponseEntity<LoginResponseDto>(userService.loginUser(loginRequestDto),
        HttpStatus.OK);

  }


}
