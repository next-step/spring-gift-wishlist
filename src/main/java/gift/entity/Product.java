package gift.entity;

import java.util.List;
import java.util.regex.Pattern;

public record Product(
        Long id,
        String name,
        int price,
        String imageUrl
) {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[\\p{IsHangul}\\p{IsHan}\\p{IsAlphabetic}\\d\\s()\\[\\]+\\-&/_]+$");

    private static final List<Pattern> MANAGEMENT_PATTERNS = List.of(
            Pattern.compile("^(?!.*카카오).*")
    );

    private static final int MAX_NAME_LENGTH = 15;

    public static Product createProduct(
            Long id,
            String name,
            int price,
            String imageUrl,
            ValidationMode mode
    ) {
        if (id != null) {
            validateId(id);
        }
        validateName(name, mode);
        validatePrice(price);
        validateImageUrl(imageUrl);
        return new Product(id, name, price, imageUrl);
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }

    private static void validateName(String name, ValidationMode validationMode) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력값입니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("상품 이름에 허용되지 않는 문자가 포함되었습니다.");
        }
        if (validationMode == ValidationMode.NORMAL) {
            validatePattern(name);
        }
    }

    private static void validatePrice(int price) {
        if (price < 1) {
            throw new IllegalArgumentException("가격은 1원 이상이어야 합니다.");
        }
    }

    private static void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수 입력값입니다.");
        }
    }

    private static void validatePattern(String name) {
        boolean valid = MANAGEMENT_PATTERNS.stream()
                .allMatch(pattern -> pattern.matcher(name).matches());
        if (!valid) {
            throw new IllegalArgumentException(
                    "상품명에 포함된 특정 문구는 담당 MD와 협의 후에만 사용할 수 있습니다."
            );
        }
    }
    
    public Product withId(Long id) {
        validateId(id);
        return new Product(id, this.name, this.price, this.imageUrl);
    }

    public Product withName(String name, ValidationMode validationMode) {
        validateName(name, validationMode);
        return new Product(this.id, name, this.price, this.imageUrl);
    }

    public Product withPrice(int price) {
        validatePrice(price);
        return new Product(this.id, this.name, price, this.imageUrl);
    }

    public Product withImageUrl(String imageUrl) {
        validateImageUrl(imageUrl);
        return new Product(this.id, this.name, this.price, imageUrl);
    }

    public enum ValidationMode {
        NORMAL,
        PERMITTED,
        DATABASE
    }
}
