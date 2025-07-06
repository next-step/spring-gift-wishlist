package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProductHiddenException extends RuntimeException {
    public ProductHiddenException(Long id) {
        super("상품 " + id + " 는 숨김 처리되어 접근할 수 없습니다.");
    }
}
