package gift.controller;

import gift.dto.UserRequestDto;
import gift.dto.UserResponseDto;
import gift.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입
     * @param userRequestDto JSON형식 이메일, 비밀번호
     * @return userResponseDto JSON형식 이메일, 비밀번호, 생성일
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> userSignUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(authService.userSignUp(userRequestDto), HttpStatus.CREATED);
    }

    /**
     * 로그인
     * @param userRequestDto JSON형식 이메일, 비밀번호
     * @return Void OK 상태코드
     */
    @PostMapping("/login")
    public ResponseEntity<Void> userLogin(@Valid @RequestBody UserRequestDto userRequestDto) {
        authService.userLogin(userRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);         // 성공했을 경우에만 OK 반환
    }
}
