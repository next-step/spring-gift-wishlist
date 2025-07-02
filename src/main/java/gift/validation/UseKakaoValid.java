package gift.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ItemCreateValidator.class, ItemUpdateValidator.class})

public @interface UseKakaoValid {
    String message() default "이름이 유효하지 않습니다.";

    /***
     * 밑에 둘 세팅은 거의 안건든다고 해서 빼보려고 했는데, 없애니까 Validator가 작동을 안한다. 왜 일까?
     *
     */
    Class<?>[] groups() default{}; //유효성 검사 그룹 지정
    Class<? extends Payload>[] payload() default {}; // 메타데이터 전달용

}
