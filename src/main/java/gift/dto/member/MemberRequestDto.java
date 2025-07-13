package gift.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestDto {

  @Email(message = "이메일 형식이여야 합니다.")
  @NotBlank(message = "이메일은 필수입니다")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다")
  private String password;

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
