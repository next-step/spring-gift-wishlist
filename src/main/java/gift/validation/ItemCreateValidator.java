package gift.validation;

import gift.dto.ItemCreateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import gift.validation.itemPolicy.LengthPolicy;
import gift.validation.itemPolicy.SpecialSymbolPolicy;
import gift.validation.itemPolicy.UseNameKaKaoPolicy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ItemCreateValidator implements ConstraintValidator<ItemFieldValid, ItemCreateDto> {

    private final ViolationHandler violationHandler;

    private List<ItemPolicy> policies;

    public ItemCreateValidator(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public void initialize(ItemFieldValid constraintAnnotation) {
        this.policies = List.of(
                new LengthPolicy(violationHandler),
                new SpecialSymbolPolicy(violationHandler),
                new UseNameKaKaoPolicy(violationHandler)
        );
    }

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (dto == null) return false;
        for (ItemPolicy policy : policies) {
            if (!policy.isValid(dto, context)) {
                return false;
            }
        }
        return true;
    }
}