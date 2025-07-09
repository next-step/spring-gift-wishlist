package gift.exception;

import org.springframework.http.HttpStatus;

public class NeedAuthorizedException extends ApplicationException{
    public NeedAuthorizedException() {
        super(HttpStatus.FORBIDDEN,"유효하지 않은 토큰입니다.");
    }
}
