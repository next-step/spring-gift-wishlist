package gift.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메서드에만 사용하도록 지정
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
}