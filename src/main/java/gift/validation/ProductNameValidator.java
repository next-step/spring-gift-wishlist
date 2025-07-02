package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    private static final String ALLOWED_SPECIAL_CHARS = "()+\\-\\[\\]&/_";
    private static final String PATTERN = "^[a-zA-Z0-9" + ALLOWED_SPECIAL_CHARS + "가-힣\\s]*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        if (value.contains("카카오")) return false;
        return value.matches(PATTERN);
    }
}