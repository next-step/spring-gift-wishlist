package gift.common.validator;

import gift.common.code.CustomResponseCode;
import gift.dto.ProductRequest;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductRequestValidator implements Validator {

    private static final List<String> FORBIDDEN_KEYWORDS = List.of("카카오");

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductRequest request = (ProductRequest) target;

        String name = request.name();
        if (name == null) {
            return;
        }

        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (name.contains(keyword)) {
                String message = String.format(CustomResponseCode.FORBIDDEN_KEYWORD.getMessage(),
                    keyword);
                errors.rejectValue("name", "forbidden-keyword", message);
                break;
            }
        }
    }
}
