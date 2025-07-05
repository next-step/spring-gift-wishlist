package gift.product.validation;

import gift.global.validation.NameType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = MustNotContainValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface NotContain {

  NameType nameType();

  String message() default "{custom.keyword.forbidden}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
