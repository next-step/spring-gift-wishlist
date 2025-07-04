package gift.controller;

import gift.dto.jwt.TokenResponse;
import gift.dto.user.CreateUserRequest;
import gift.dto.user.LoginRequest;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid CreateUserRequest request) {
        userService.saveUser(request);
        return ResponseEntity.noContent().build();
    }


//    @PostMapping
//    public void login(@RequestBody LoginRequest request) {
//        TokenResponse tokens = userService.login(request);
//
//    }
}
