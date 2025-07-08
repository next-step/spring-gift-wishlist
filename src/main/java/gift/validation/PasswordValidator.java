package gift.validation;

import gift.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return String.class.equals(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    String password = (String) obj;

    if (password == null || password.length() == 0) {
      errors.rejectValue("password", ErrorCode.INVALID_PASSWORD.getCode(), "비밀번호는 반드시 입력되어야 합니다.");
      return;
    }

    if (password.chars().filter(Character::isUpperCase).count() < 1) {
      errors.rejectValue("password", ErrorCode.INVALID_PASSWORD.getCode(),
          "비밀번호에는 반드시 1글자 이상의 대문자가 포함되어야 합니다.");
    }

    if (password.chars().filter(Character::isDigit).count() < 3) {
      errors.rejectValue("password", ErrorCode.INVALID_PASSWORD.getCode(),
          "비밀번호에는 반드시 숫자가 3개 이상 포함되어야 합니다.");
    }

    if (password.chars().filter(ch -> "!@#$,".indexOf(ch) != -1).count() < 1) {
      errors.rejectValue("password", ErrorCode.INVALID_PASSWORD.getCode(),
          "비밀번호에는 반드시 !,@,#,$,. 중 하나 이상의 특수문자가 포함되어야 합니다.");
    }
  }


}
