package gift.validation;

import gift.dto.WishRequest;
import gift.entity.Member;
import gift.exception.InvalidFieldException;
import gift.exception.ValidationException;

public class ValidationUtil {

    public static void validateWishRequestAndMember(WishRequest request, Member member) {
        if (request == null) {
            throw new ValidationException("Request cannot be null");
        }
        if (member == null) {
            throw new ValidationException("Member cannot be null");
        }
        if (request.productId() == null) {
            throw new InvalidFieldException("Invalid productId");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new InvalidFieldException("Invalid quantity");
        }
    }

    public static void validatePIDAndMember(Long productId, Member member) {
        if (productId == null) {
            throw new ValidationException("ProductId cannot be null");
        }
        if (member == null) {
            throw new ValidationException("Member cannot be null");
        }
    }
}
