package gift.dto;

import gift.model.Member;
public class MemberDto {
  private Long id;
  private String email;
  private String password;

  public MemberDto() {}

  public MemberDto(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public static MemberDto from(Member member) {
    if(member == null){
      return null;
    }
    return new MemberDto(
        member.getId(),
        member.getEmail(),
        member.getPassword()
    );
  }

  public Member toEntity() {
    return new Member(
        id,
        email,
        password
    );
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}
