package gift.exception.forbidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
