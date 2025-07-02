package gift.validation;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductNameValidator.class)
public @interface ValidProductName {

    String message() default "특수 문자는 ((, ), [, ], +, -, &, /, _)만 가능합니다. "
        + "또는 '카카오'가 포함된 문구는 담당 MD와 협의가 필요합니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
