package gift.exception.common;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
