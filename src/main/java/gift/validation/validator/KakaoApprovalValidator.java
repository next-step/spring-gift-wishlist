package gift.validation.validator;

import gift.validation.annotation.KakaoApprovalRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class KakaoApprovalValidator implements ConstraintValidator<KakaoApprovalRequired, Object> {

    private String nameField;
    private String approvalField;

    @Override
    public void initialize(KakaoApprovalRequired constraintAnnotation) {
        this.nameField = constraintAnnotation.nameField();
        this.approvalField = constraintAnnotation.approvalField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        String name = (String) wrapper.getPropertyValue(nameField);
        if (name == null || !name.contains("카카오")) {
            return true;
        }

        Boolean isMdApproved = (Boolean) wrapper.getPropertyValue(approvalField);
        return Boolean.TRUE.equals(isMdApproved);
    }
}
