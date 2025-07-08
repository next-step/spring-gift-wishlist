package gift.exception;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException(String message) {
        super("로그인 실패: " + message);
    }
}
