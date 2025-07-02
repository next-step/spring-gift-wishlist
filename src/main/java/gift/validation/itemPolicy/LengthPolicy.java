package gift.validation.itemPolicy;

import gift.dto.ItemCreateDto;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;

public class LengthPolicy implements ItemPolicy {
    private final ViolationHandler violationHandler;

    public LengthPolicy(ViolationHandler violationHandler) {
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
