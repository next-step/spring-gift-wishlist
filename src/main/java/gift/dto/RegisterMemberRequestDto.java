package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterMemberRequestDto(
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        String password
) {
}
