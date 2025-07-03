package gift.product.validation;

import gift.global.validation.NameBlacklist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class MustNotContainValidator implements ConstraintValidator<NotContain,String> {
  private List<String> blacklist;

  @Override
  public void initialize(NotContain constraintAnnotation) {
    blacklist = NameBlacklist.getBlacklist(constraintAnnotation.nameType());
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (s == null) return true;

    boolean isValid = blacklist.stream().noneMatch(s::contains);
    if (!isValid) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          "상품명에 다음 키워드를 포함할 수 없습니다: " + String.join(", ", blacklist)
      ).addConstraintViolation();
    }

    return isValid;
  }
}
