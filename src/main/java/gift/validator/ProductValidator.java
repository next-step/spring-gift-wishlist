package gift.validator;

import gift.entity.Product;
import gift.exception.RequestValidateFailException;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    private static final String NAME_RULE = "^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,15}$";
    private static final String INVOLVE_KAKAO = ".*카카오.*";

    public boolean validate(Product product) {
        String name = product.getName();
        if (!name.matches(NAME_RULE)) {
            throw new RequestValidateFailException("영문, 한글, 숫자, ()[]+-&/_ 를 사용한 15이하 이름만 허용됨: " + name);
        }
        return !name.matches(INVOLVE_KAKAO);
    }
}
