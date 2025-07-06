package gift.entity.product.value;

import java.util.Objects;
import java.util.regex.Pattern;

public record ProductName(String value) {

    public static final String ALLOWED_CHAR =
            "^[\\p{IsHangul}\\p{IsHan}\\p{IsAlphabetic}\\d\\s()\\[\\]+\\-&/_]+$";

    public static final int MAX_LENGTH = 15;

    public ProductName {
        Objects.requireNonNull(value, "상품 이름은 필수 입력값입니다.");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력값입니다.");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 최대 " + MAX_LENGTH + "자까지 입력할 수 있습니다.");
        }
        if (!Pattern.compile(ALLOWED_CHAR).matcher(trimmed).matches()) {
            throw new IllegalArgumentException("상품 이름에 허용되지 않는 문자가 포함되었습니다.");
        }

        value = trimmed;
    }
}
