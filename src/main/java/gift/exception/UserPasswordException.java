package gift.exception;

import org.springframework.http.HttpStatus;

public class UserPasswordException extends ApplicationException{
    public UserPasswordException() {
        super(HttpStatus.NOT_FOUND,"비밀번호가 틀렸습니다.");
    }
}
