package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "이메일을 반드시 입력해야 합니다.")
    @Email String email,

    @NotBlank(message = "비밀번호를 반드시 입력해야 합니다.")
    String password
) {
}
