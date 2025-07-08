package gift.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductNamePatternValidator.class)
public @interface ProductNamePattern {

    String message() default "{gift.validation.ProductNamePattern.message}"; // @NotBlank 양식을 참고함

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
