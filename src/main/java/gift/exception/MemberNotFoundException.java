package gift.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super(email + "은 가입되지 않은 이메일입니다.");
    }
}
