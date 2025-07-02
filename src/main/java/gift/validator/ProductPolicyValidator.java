package gift.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductPolicyValidator implements ConstraintValidator<ProductPolicy, ProductPolicyProvider> {

    @Override
    public boolean isValid(ProductPolicyProvider dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        String name = dto.name();
        boolean mdApproved = dto.mdApproved();

        if (name == null || !name.contains("카카오")) {
            return true;
        }
        return mdApproved;
    }
}