package gift.controller;

import gift.dto.api.MemberRegisterRequestDto;
import gift.dto.api.MemberRegisterResponseDto;
import gift.exception.InvalidAuthorizationHeaderException;
import gift.exception.InvalidCredentialsException;
import gift.exception.MissingAuthorizationHeaderException;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 생성
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponseDto> createMember(
        @RequestBody @Valid MemberRegisterRequestDto requestDto
    ) {
        String token = memberService.registerMember(requestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new MemberRegisterResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberRegisterResponseDto> login(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) throws InvalidCredentialsException {
        if (authorizationHeader == null) {
            // 헤더 누락 → 401 Unauthorized
            throw new MissingAuthorizationHeaderException("Authorization 헤더가 필요합니다.");
        }
        if (!authorizationHeader.startsWith("Basic ")) {
            // 헤더 형식 오류 → 401 Unauthorized
            throw new InvalidAuthorizationHeaderException("Authorization 헤더 형식이 올바르지 않습니다.");
        }

        // Base64 디코딩
        String base64Cred = authorizationHeader.substring(6).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Cred);
        String cred = new String(decoded, StandardCharsets.UTF_8);
        String[] parts = cred.split(":", 2);
        if (parts.length != 2) {
            // 디코딩 결과 형식 오류 → 401
            throw new InvalidAuthorizationHeaderException("Authorization 헤더 형식이 올바르지 않습니다.");
        }

        String email = parts[0];
        String rawPassword = parts[1];

        try {
            String token = memberService.loginMember(email, rawPassword);
            return ResponseEntity.ok(new MemberRegisterResponseDto(token));
        } catch (IllegalArgumentException ex) {
            // 잘못된 자격 증명 → 403 Forbidden
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

    }

}
