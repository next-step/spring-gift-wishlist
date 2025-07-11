package gift.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginMemberDto {


    private Long id;

    @NotBlank(message = "이메일는 필수 입력 값입니다.")
    private String email;

    public LoginMemberDto(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
