package gift.validation;

import jakarta.validation.*;


public class NoKakaoValidator implements ConstraintValidator<NoKakao, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.contains("카카오");
    }
}
