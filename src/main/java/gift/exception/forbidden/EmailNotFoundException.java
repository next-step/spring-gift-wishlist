package gift.exception.forbidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("존재하지 않는 이메일입니다.");
    }
}
