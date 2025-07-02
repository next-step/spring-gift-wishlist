package gift.validation.annotation;

import gift.validation.validator.KakaoApprovalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = KakaoApprovalValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KakaoApprovalRequired {
    String message() default "\"카카오\"가 포함된 상품명은 MD 승인(isMdApproved=true)이 필요합니다.";
    String nameField();
    String approvalField();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}