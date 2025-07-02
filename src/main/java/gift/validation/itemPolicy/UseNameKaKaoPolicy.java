package gift.validation.itemPolicy;

import gift.dto.ItemCreateDto;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;

public class UseNameKaKaoPolicy implements ItemPolicy {
    private final ViolationHandler violationHandler;

    public UseNameKaKaoPolicy(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (!dto.useKakaoName() && dto.name().contains("카카오")) {
            violationHandler.addViolation(context, "\"카카오\"는 MD와 협의한 경우에만 사용할 수 있습니다.");

            return false;
        }
        return true;
    }
}
