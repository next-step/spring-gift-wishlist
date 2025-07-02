package gift.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣\\s\\[\\]+\\-&/_]*$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            makeTemplate(context, "상품 이름을 입력해 주세요");
            return false;
        }

        if (value.length() > 15) {
            makeTemplate(context, "상품 이름은 최대 15자입니다.");
            return false;
        }

        if (value.contains("카카오")) {
            makeTemplate(context, "상품 이름에 '카카오'는 포함될 수 없습니다. 담당 MD와 협의해 주세요.");
            return false;
        }

        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            makeTemplate(context, "상품 이름에 허용되지 않은 특수문자가 포함돼 있습니다.(허용 : ( ), [ ], +, -, &, /, _)");
            return false;
        }
        return true;
    }

    public void makeTemplate(ConstraintValidatorContext context, String msg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
