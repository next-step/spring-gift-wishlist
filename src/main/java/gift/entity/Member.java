package gift.entity;

import jakarta.validation.constraints.Email;

public class Member {

  private Long id;

  @Email(message = "이메일 형식으로 들어와야 합니다")
  private String email;
  
  private String password;

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public Member(String email, String password) {
    this.email = email;
    this.password = password;
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
}
