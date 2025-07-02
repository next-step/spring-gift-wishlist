package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class NoKakaoValidator implements ConstraintValidator<NoKakao, String> {

    private static final Set<String> kakaoWhitelist = Set.of("카카오 초콜릿");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value.contains("카카오") && !kakaoWhitelist.contains(value)) {
            return false;
        }

        return true;
    }
}


