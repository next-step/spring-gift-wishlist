package gift.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ItemCreateValidator.class)

public @interface UseKakaoValid {
    String message() default "이름이 유효하지 않습니다.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}
