package gift.controller;

import gift.annotation.AuthUser;
import gift.dto.request.UserModifyRequest;
import gift.dto.response.UserResponse;
import gift.service.TokenService;
import gift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthUser UserResponse user) {
        return ResponseEntity.ok().body(user);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> modifyUser(
            @PathVariable Long userId,
            @RequestBody UserModifyRequest userModifyRequest
    ){
        userService.modifyUserInfo(userId, userModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId
    ){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
