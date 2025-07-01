package gift.product.validator;

import gift.product.annotation.ProductNameValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameConstraint implements ConstraintValidator<ProductNameValidator, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.contains("카카오")) return  false;
        return true;
    }
}
