package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidBearerAuthException extends IllegalArgumentException {

    public InvalidBearerAuthException() {
        super("잘못된 Bearer 인증 헤더입니다");
    }
}
