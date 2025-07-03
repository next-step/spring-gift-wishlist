package gift.product.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = gift.product.validator.ProductNameValidator.class)
public @interface ProductNameConstraint {

    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}