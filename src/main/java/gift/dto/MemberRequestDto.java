package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestDto {

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @Email(message = "이메일 형식과 맞지 않습니다.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private final String password;

    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
