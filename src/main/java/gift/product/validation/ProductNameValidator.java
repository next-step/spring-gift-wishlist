package gift.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
<<<<<<< HEAD
=======

>>>>>>> 8db4ff8 (feat(product): Aggregate all product name validation errors)
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣\\s\\[\\]+\\-&/_]*$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        List<String> errMsgs = new ArrayList<>();

        if (value == null || value.isBlank()) {
            errMsgs.add("상품 이름을 입력해 주세요");
        }
        else {
            if (value.length() > 15) {
                errMsgs.add("상품 이름은 최대 15자입니다.");
            }

            if (value.contains("카카오")) {
                errMsgs.add("상품 이름에 '카카오'는 포함될 수 없습니다. 담당 MD와 협의해 주세요.");
            }

            if (!ALLOWED_PATTERN.matcher(value).matches()) {
                errMsgs.add("상품 이름에 허용되지 않은 특수문자가 포함돼 있습니다.(허용 : ( ), [ ], +, -, &, /, _)");
            }
        }

        if (!errMsgs.isEmpty()){
            String finalMsg = String.join("\n", errMsgs);
            makeTemplate(context, finalMsg);
            return false;
        }

        return true;
    }

    public void makeTemplate(ConstraintValidatorContext context, String msg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
