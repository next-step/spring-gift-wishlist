package gift.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super("인증 실패 : " + message);
    }
}
