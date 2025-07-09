package gift.dto.member;

import gift.entity.Member;

public class MemberResponseDto {

  private Long id;
  private String email;
  private String password;

  public MemberResponseDto(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public MemberResponseDto(Member member) {
    this(member.getId(), member.getEmail(), member.getPassword());
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
}
