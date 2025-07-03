package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class RequiresApprovalValidator implements ConstraintValidator<RequiresApproval, String> {

    private static final List<String> APPROVAL_REQUIRED_WORDS = List.of("카카오");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return APPROVAL_REQUIRED_WORDS.stream().noneMatch(s::contains);
    }
}
