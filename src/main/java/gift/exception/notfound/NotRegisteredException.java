package gift.exception.notfound;

import gift.exception.common.NotFoundException;

public class NotRegisteredException extends NotFoundException {
    public NotRegisteredException() {
        super("등록되지 않은 계정입니다.");
    }
}
