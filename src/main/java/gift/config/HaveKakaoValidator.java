package gift.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HaveKakaoValidator implements ConstraintValidator<HaveKakao, String> {

    String required;

    @Override
    public void initialize(HaveKakao constraintAnnotation) {
        this.required = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return !value.contains(required);
    }
}
