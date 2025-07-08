package gift.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Member {

  private Long id;

  @Email(message = "이메일 형식으로 들어와야 합니다")
  @NotBlank(message = "이메일은 필수입니다")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다")
  private String password;

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public Member(String email, String password) {
    this(null, email, password);
  }

  public Member() {
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isPasswordMatch(String password) {
    return this.password.equals(password);
  }
}
