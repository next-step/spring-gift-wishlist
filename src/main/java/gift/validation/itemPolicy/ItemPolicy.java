package gift.validation.itemPolicy;

import gift.dto.ItemCreateDto;
import jakarta.validation.ConstraintValidatorContext;

public interface ItemPolicy<T> {
    boolean isValid(T dto, ConstraintValidatorContext context);
}