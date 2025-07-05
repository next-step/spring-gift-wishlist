package gift.controller.userController;


import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;
import gift.entity.User;
import gift.service.itemService.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping()
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestBody @Valid UserRegisterDto dto,
            Model model
    ) {
        UserResponseDto user =userService.registerUser(dto);
        return new ResponseEntity<UserResponseDto>(user, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUserList(
            @RequestParam(required = false) String email,
            Model model
    ) {
        List<UserResponseDto> users = userService.getUserList(email);

        return new ResponseEntity<List<UserResponseDto>>(users, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserResponseDto> deleteUser(
            @RequestParam Long id,
            Model model
    ) {
        UserResponseDto response = userService.finUserById(id);

        return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
    }
}
