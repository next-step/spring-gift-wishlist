package gift.exception.common;

import org.springframework.http.HttpStatus;

public class NoContentException extends HttpException {
    public NoContentException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NO_CONTENT;
    }
}
