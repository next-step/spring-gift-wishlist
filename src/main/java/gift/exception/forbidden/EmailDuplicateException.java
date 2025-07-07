package gift.exception.forbidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException() {
        super("이미 존재하는 이메일입니다.");
    }
}
