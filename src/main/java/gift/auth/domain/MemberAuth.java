package gift.auth.domain;

public record MemberAuth (
    Long memberId,
    String email,
    String password,
    String refreshToken
){
  public MemberAuth{
    validateEmail(email);
    validatePassword(password);
    validateToken(refreshToken);
  }

  public static MemberAuth of(String email, String password){
    return new MemberAuth(null, email, password, null);
  }
  public static MemberAuth of(String email, String password, String refreshToken){
    return new MemberAuth(null,email,password,refreshToken);
  }
  public static MemberAuth withId(Long memberId, String email,String password, String refreshToken){
    validateId(memberId);
    return new MemberAuth(memberId,email, password, refreshToken);
  }

  private static void validateId(Long id){
    if(id==null || id<0) {
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
  private static void validateToken(String token){
    if(token==null || token.isBlank()){
      throw new IllegalArgumentException("token은 null이거나 빈 값일 수 없습니다.");
    }
  }
}
