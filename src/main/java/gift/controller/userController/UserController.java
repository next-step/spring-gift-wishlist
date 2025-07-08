package gift.controller.userController;


import gift.Jwt.JwtUtil;
import gift.dto.userDto.UserLoginDto;
import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import gift.service.userService.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /***
     * Todo. 관리자 User 관리 페이지를 위한 model 생성
     *
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid UserRegisterDto dto) {
        String token = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid UserLoginDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", userService.loginUser(dto)));
    }


    @GetMapping()
    public ResponseEntity<?> getUserList(@RequestHeader("Authorization") String authHeader, @RequestParam(required = false) String email, Model model) {

        String token = extractToken(authHeader);

        if (!jwtUtil.validate(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        if (!isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        List<UserResponseDto> users = userService.getUserList(email, true);
        return ResponseEntity.ok(users);
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }

    private boolean isAdmin(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get("role", String.class);
        return "ADMIN".equals(role);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(@RequestParam Long id, Model model) {
        userService.deleteUserById(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        UserResponseDto updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(updatedUser);
    }
}
