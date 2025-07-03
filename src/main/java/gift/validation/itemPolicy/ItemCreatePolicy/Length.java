package gift.validation.itemPolicy.ItemCreatePolicy;

import gift.dto.ItemCreateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class Length implements ItemPolicy<ItemCreateDto> {
    private final ViolationHandler violationHandler;

    public Length(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (dto.name().length() > 15) {
            violationHandler.addViolation(context,"상품 이름은 최대 15자까지 입니다.");
            return false;
        }
        return true;
    }
}
