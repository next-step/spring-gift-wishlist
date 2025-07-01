package gift.product.annotation;

import gift.product.validator.ProductNameConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ProductNameConstraint.class)
public @interface ProductNameValidator {

    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}