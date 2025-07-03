package gift.common.validator;

import gift.common.code.CustomResponseCode;
import gift.dto.ProductRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductRequestValidator implements Validator {

    private static final String FORBIDDEN_KEYWORD = "카카오";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProductRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductRequest request = (ProductRequest) target;

        if (request.name() != null && request.name().contains(FORBIDDEN_KEYWORD)) {
            errors.rejectValue("name", "forbidden-keyword",
                CustomResponseCode.FORBIDDEN_KEYWORD_KAKAO.getMessage());
        }
    }
}
