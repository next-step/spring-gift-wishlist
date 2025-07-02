package gift.common.annotation.validator;

import gift.common.annotation.NameValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameValidation, String> {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 15;
    private static final String ALLOWED_PATTERN = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&/_ ]*$";
    private static final String NOT_ALLOWED_WORD = "카카오";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int length = value.length();

        if (MIN_LENGTH > length || MAX_LENGTH < length) { // 상품명 길이 검증
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "상품 이름은 공백을 포함하여 최대 15글자까지 입력할 수 있습니다."
            ).addConstraintViolation();
            return false;
        }

        if (!value.matches(ALLOWED_PATTERN)) { // 상품명 패턴 검증
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "상품 이름은 한글, 영어, 숫자, 특수문자(( ), [ ], +, -, &, /, _) 외 다른 문자가 들어갈 수 없습니다."
            ).addConstraintViolation();
            return false;
        }

        if (value.contains(NOT_ALLOWED_WORD)) { // 상품명에 '카카오' 포함 여부 검증
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다."
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

}
