package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        String password,

        @NotBlank(message = "역할은 비어있을 수 없습니다.")
        String role
){}