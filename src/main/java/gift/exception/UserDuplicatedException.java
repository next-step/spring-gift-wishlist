package gift.exception;

import org.springframework.http.HttpStatus;

public class UserDuplicatedException extends ApplicationException {
    public UserDuplicatedException() {
        super(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
    }
}