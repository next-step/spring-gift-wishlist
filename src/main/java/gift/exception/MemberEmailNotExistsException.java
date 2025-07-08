package gift.exception;

public class MemberEmailNotExistsException extends RuntimeException {
    public MemberEmailNotExistsException() {
        super("가입되지 않은 이메일입니다.");
    }
}
