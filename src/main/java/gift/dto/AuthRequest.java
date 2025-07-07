package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일은 형식이 아닙니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(max = 20, message = "비밀번호는 최대 20자까지만 입력 가능합니다.")
    String password
) {

}
