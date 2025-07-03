package gift.validation;

public class ProductNameValidator {
    private static final String ALLOWED_CHAR_PATTERN = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&/_]*$";

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 비어 있을 수 없습니다.");
        }

        if (name.length() > 15) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.");
        }

        if (!name.matches(ALLOWED_CHAR_PATTERN)) {
            throw new IllegalArgumentException("상품 이름에는 ( ), [ ], +, -, &, /, _ 의 특수 문자만 사용할 수 있습니다.");
        }

        if (name.contains("카카오")) {
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 MD 승인이 필요합니다.");
        }
    }
}
