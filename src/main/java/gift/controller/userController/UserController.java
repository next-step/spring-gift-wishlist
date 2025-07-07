package gift.controller.userController;


import gift.dto.userDto.UserLoginDto;
import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import gift.service.userService.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<List<UserResponseDto>> getUserList(@RequestParam(required = false) String email, Model model) {
        List<UserResponseDto> users = userService.getUserList(email);

        return new ResponseEntity<List<UserResponseDto>>(users, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
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
