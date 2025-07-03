package gift.validation.itemPolicy.ItemCreatePolicy;

import gift.dto.ItemCreateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;


@Component
public class SpecialSymbol implements ItemPolicy<ItemCreateDto> {

    private final ViolationHandler violationHandler;

    public SpecialSymbol(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9()\\[\\]+\\-\\&/_가-힣ㄱ-ㅎㅏ-ㅣ\\s]*$");

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (!pattern.matcher(dto.name()).matches()) {
            violationHandler.addViolation(context,"( ), [ ], +, -, &, /, _\" 외에는 특수 문자가 허용되지 않습니다.");
            return false;
        }
        return true;
    }
}
