package gift.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank(message = "이메일은 반드시 입력되어야 합니다.")
    String email,

    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.")
    String password
) {

}
