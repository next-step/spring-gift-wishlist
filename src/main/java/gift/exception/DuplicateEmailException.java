package gift.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateEmailException extends DataIntegrityViolationException {

    public DuplicateEmailException() {
        super("이미 가입된 이메일입니다.");
    }
}
