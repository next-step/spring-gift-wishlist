package gift.validation;

public class ProductValidator {
    public static void validateName(String name) {
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 MD 승인이 필요합니다.");
        }
    }
}
