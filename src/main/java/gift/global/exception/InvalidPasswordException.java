package gift.global.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
}
