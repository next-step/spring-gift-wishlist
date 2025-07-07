package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password
) {
}
