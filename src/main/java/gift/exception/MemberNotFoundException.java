package gift.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super("계정을 찾을 수 없습니다: " + message);
    }
}
