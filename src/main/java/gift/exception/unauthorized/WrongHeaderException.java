package gift.exception.unauthorized;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongHeaderException extends RuntimeException {

    public WrongHeaderException() {
        super("잘못된 인증 헤더입니다.");
    }
}
