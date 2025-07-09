package gift.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApplicationException{
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
    }
}
