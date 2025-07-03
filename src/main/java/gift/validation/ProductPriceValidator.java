package gift.validation;

public class ProductPriceValidator {
    public static void validatePrice(Long price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 필수 입력입니다.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }
}
