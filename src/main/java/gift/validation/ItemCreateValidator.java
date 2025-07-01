package gift.validation;

import gift.dto.ItemCreateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ItemCreateValidator implements ConstraintValidator<UseKakaoValid, ItemCreateDTO> {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9()\\[\\]+\\-\\&/_]*$");

    @Override
    public boolean isValid(ItemCreateDTO dto, ConstraintValidatorContext context) {
        boolean isValid = false;
        if (dto == null) {
            return false;
        }
        if (dto.name().length() > 15) {
            isValid = false;
            addViolation(context,"상품 이름은 최대 15자까지 입니다.");
        }
        if (!pattern.matcher(dto.name()).matches()) {
            isValid =false;
            addViolation(context,"( ), [ ], +, -, &, /, _\" 외에는 특수 문자가 허용되지 않습니다.");
        }
        if (!dto.isValid() && dto.name().contains("카카오")) {
            isValid = false;
            addViolation(context, "\"카카오\"는 MD와 협의한 경우에만 사용할 수 있습니다.");
        }

        return isValid;
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("name")
                .addConstraintViolation();
    }
}
