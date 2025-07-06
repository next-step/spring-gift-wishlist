package gift.member.domain;

public record Member(
    Long id, String email, String password
){
  public Member{
    validateEmail(email);
    validatePassword(password);
  }

  public static Member of(String email, String password){
    return new Member(null,email, password);
  }
  public static Member withId(Long id, String email, String password){
    validateId(id);
    return new Member(id,email,password);
  }

  private static void validateId(Long id){
    if(id==null || id<=0) {
      throw new IllegalArgumentException("id값은 null이거나 음수일 수 없습니다.");
    }
  }
  private static void validateEmail(String email){
    if(email==null || email.isBlank()) {
      throw new IllegalArgumentException("이메일은 null이거나 빈 값일 수 없습니다,");
    }
  }
  private static void validatePassword(String password){
    if(password==null || password.isBlank()){
      throw new IllegalArgumentException("비밀번호는 null이거나 빈 값일 수 없습니다.");
    }
  }
}
