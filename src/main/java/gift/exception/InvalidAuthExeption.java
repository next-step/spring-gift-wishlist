package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAuthExeption extends IllegalArgumentException {

    public InvalidAuthExeption() {
        super("잘못된 이메일·비밀번호입니다");
    }
}
