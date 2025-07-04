package gift.exception.forbidden;

import gift.exception.common.ForbiddenException;

public class WrongPasswordException extends ForbiddenException {
    public WrongPasswordException() {
        super("잘못된 비밀번호입니다.");
    }
}
