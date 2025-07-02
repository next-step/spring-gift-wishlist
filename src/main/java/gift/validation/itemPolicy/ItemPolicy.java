package gift.validation.itemPolicy;

import gift.dto.ItemCreateDto;
import jakarta.validation.ConstraintValidatorContext;

public interface ItemPolicy {
    boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context);
}
